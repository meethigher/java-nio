package top.meethigher.c3.pro;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class WriteClient {
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += sc.read(buffer);
            System.out.println(count);
            buffer.clear();
            if(count==30001002) {
                break;
            }
        }
        ByteBuffer 一拳打爆江诗彼岸 = StandardCharsets.UTF_8.encode("一拳打爆江诗彼岸");
        System.out.println(一拳打爆江诗彼岸.capacity());
        sc.write(一拳打爆江诗彼岸);
        System.in.read();
    }
}
