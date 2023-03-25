package top.meethigher.c3;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Client {
    public static void main(String[] args) throws Exception {
        SocketChannel sc=SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1",8080));

        Thread.sleep(5000);
        //使用单线程阻塞模式，并不能及时的监听到消息
        sc.write(StandardCharsets.UTF_8.encode("world"));

        System.in.read();
    }
}
