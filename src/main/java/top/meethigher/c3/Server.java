package top.meethigher.c3;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import static top.meethigher.c1.ByteBufferUtil.debugRead;

@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        //使用nio实现来理解阻塞模式, 单线程处理

        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        //1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //1.1 设置ServerSocketChannel为非阻塞(默认是阻塞)。这会导致accept方法非阻塞
        ssc.configureBlocking(false);
        //2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3 建立连接的集合
        List<SocketChannel> channels = new LinkedList<>();
        while (true) {
            //4 accept 建立与客户端的连接 SocketChannel用来与客户端通信
            //log.info("connecting...");
            //上面设置了非阻塞，那么这个就不等待，直接返回。
            //如果没有连接建立，返回的就是空
            SocketChannel channel = ssc.accept();
            if (channel != null) {
                log.info("connected... {}", channel);
                //SocketChannel设置为非阻塞(默认是阻塞)，这会导致read方法非阻塞
                channel.configureBlocking(false);
                channels.add(channel);
            }
            for (SocketChannel socketChannel : channels) {
                //上面设置了非阻塞，如果没有读到数据，就返回
                int len = socketChannel.read(byteBuffer);
                if (len > 0) {
                    //5 接收客户端发送的数据
                    log.info("before read... {}", socketChannel);
                    byteBuffer.flip();
                    debugRead(byteBuffer);
                    byteBuffer.clear();
                    log.info("after read... {}", socketChannel);
                }
            }
        }
    }
}
