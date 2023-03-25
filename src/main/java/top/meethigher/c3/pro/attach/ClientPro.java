package top.meethigher.c3.pro.attach;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientPro {

    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        while (true) {
            System.in.read();
            ByteBuffer encode = StandardCharsets.UTF_8.encode("清漪清漪清漪\n你好你好你好\n世界世界世界\n");
            sc.write(encode);
            log.info("发送成功");
        }
    }
}
