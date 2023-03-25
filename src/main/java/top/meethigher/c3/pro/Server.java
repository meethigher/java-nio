package top.meethigher.c3.pro;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static top.meethigher.c1.ByteBufferUtil.debugRead;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //1. 创建Selector, 用来管理多个Channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //2. 建立Selector与ServerSocketChannel之间的联系(将Channel注册到Selector)
        // SelectionKey就是将来事件发生后, 通过他可以知道事件和哪个channel的事件
        // 0表示不监听事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //测试往selector上面注册多个ssc
//        ServerSocketChannel tempSsc = ServerSocketChannel.open();
//        tempSsc.configureBlocking(false);
//        tempSsc.register(selector, 0, null);
        log.info("register key: {}", sscKey);
        // 配置监听的事件, 只监听连接事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            //3. 调用Selector的select方法, select是阻塞方法。没有事件发生，线程阻塞(cpu闲置)
            // select 在事件未处理时(调用accept或者cancel表示已处理), 它不会阻塞
            selector.select();
            //4. 处理事件, selectedKeys内部包含了所有发生的事件
            Set<SelectionKey> keys = selector.keys();
            log.info("all keys size: {}", keys.size());
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            log.info("keys size: {}", selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.info("key: {}", key);
                iterator.remove();//不执行删除，会存在下一次别的key触发事件时，上一个key还存在里面
                //5. 区分事件类型
                if (key.isAcceptable()) {//建立连接accept
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //非阻塞下返回为null表示没有连接建立，这时候八成是keys里面没有移除。
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    log.info("{}", sc);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                } else if (key.isReadable()) {//接收消息 read
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                        int read = channel.read(byteBuffer);//如果是正常断开，返回值是-1
                        if (read == -1) {
                            key.cancel();
                        } else {
                            // 打印
                            byteBuffer.flip();
                            debugRead(byteBuffer);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();//如果客户端关闭了连接，要取消注册，会在keys中彻底删除
                    }
                }
            }
        }
    }
}
