package top.meethigher.c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

public class TestByteBufferFlip {

    public static void main(String[] args) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("abcd");
        debugAll(buffer);

        buffer.flip();
        debugAll(buffer);
//        buffer.limit(3);
        buffer.flip();
        debugAll(buffer);
    }
}
