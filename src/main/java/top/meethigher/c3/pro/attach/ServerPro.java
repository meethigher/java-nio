package top.meethigher.c3.pro.attach;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerPro {

    private static void handle(ByteBuffer buffer) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            //找到一条完整消息
            if (buffer.get(i) == '\n') {
                int length = i + 1 - buffer.position();
                //把这条消息存入新的ByteBuffer
                ByteBuffer byteBuffer = ByteBuffer.allocate(length);
                //从buffer读，像byteBuffer写
                for (int j = 0; j < length; j++) {
                    byteBuffer.put(buffer.get());
                }
                byteBuffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
            }
        }
        //移动到未读的位置。执行到position、limit相等时，就应该扩容。
        buffer.compact();
    }

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    //第三个参数，表示注册的附件。每个channel独有的附件。
                    SelectionKey scKey = sc.register(selector, 0, ByteBuffer.allocate(10));
                    scKey.interestOps(SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        int read = channel.read(byteBuffer);
                        if (read == -1) {//正常断开
                            key.cancel();
                        } else {
//                            byteBuffer.flip();
//                            System.out.println(byteBuffer.limit());
//                            System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
                            handle(byteBuffer);
                            log.info("执行");
//                            //执行到position、limit相等时，就应该扩容。
//                            if (byteBuffer.position() == byteBuffer.limit()) {
//                                log.info("扩容");
//                                ByteBuffer newByteBuffer = ByteBuffer.allocate(byteBuffer.capacity() * 2);
//                                byteBuffer.flip();
//                                newByteBuffer.put(byteBuffer);
//                                key.attach(newByteBuffer);
//                            }
                        }
                    } catch (IOException e) {//异常断开
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }
}
