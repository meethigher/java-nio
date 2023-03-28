package top.meethigher.c3.pro;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
public class WriteServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    //((ServerSocketChannel) key.channel()).accept();
                    //写法作用同上
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    //向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30001002; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(sb.toString());
                    int write = sc.write(buffer);
                    log.info("写出字节数{}", write);
                    if (buffer.hasRemaining()) {
                        //获取到已经关注的事件，再额外加上写事件。
                        //读1 写4 连接成功8 接收连接16
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);//注意越加越多
                        //scKey.interestOps(scKey.interestOps() | SelectionKey.OP_WRITE);//效果同上
                        //未写完的数据，挂到scKey
                        scKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    log.info("写出字节数{}", write);
                    //清理
                    if (!buffer.hasRemaining()) {
                        key.attach(null);
                        //不再关注可写事件
                        //channel关注了可写事件，那么只要缓冲区可写就会触发，所以应该在写完之后，取消关注write
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                } else if (key.isReadable()) {
                    log.info("可读");
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer attachment = (ByteBuffer) key.attachment();
                    if (attachment == null) {
                        attachment = ByteBuffer.allocate(5);
                        key.attach(attachment);
                    }
                    try {
                        int read = sc.read(attachment);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            attachment.clear();
                        }
                    } catch (Exception e) {
                        key.cancel();
                    }
                }
            }
        }
    }
}
