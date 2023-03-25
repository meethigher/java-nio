package top.meethigher.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

/**
 * 测试分散读取
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/3/16 21:20
 */
public class TestScatteringReads {
    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("words.txt", "r").getChannel()) {
            //准备多个buffer
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{b1, b2, b3});
            //此时刚把值写到ByteBuffer，此时三个buffer的position与limit分别都到了3、3、5。进行翻转。将limit置为当前position，position置为0
            b1.flip();
            b2.flip();
            b3.flip();
            debugAll(b1);
            debugAll(b2);
            debugAll(b3);
        } catch (IOException e) {
        }
    }
}
