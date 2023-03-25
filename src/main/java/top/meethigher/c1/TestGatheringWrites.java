package top.meethigher.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 集中写
 */
public class TestGatheringWrites {

    public static void main(String[] args) {
        //准备多个buffer
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("one");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("two");
        //utf-8中，中文占3个字节
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");

        try (FileChannel channel = new RandomAccessFile("words2.txt", "rw").getChannel()) {
            channel.write(new ByteBuffer[]{b1, b2, b3});
        } catch (IOException e) {
        }

    }
}
