package top.meethigher.c1;

import java.nio.ByteBuffer;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //添加字符
        buffer.put((byte) 0x61);//字符a
        debugAll(buffer);

        //添加3个字符
        buffer.put(new byte[]{0x62, 0x63, 0x64});
        debugAll(buffer);
//        System.out.println(buffer.get());

        //切换为读模式
        buffer.flip();
        debugAll(buffer);

        //读一个
        System.out.println(buffer.get());
        debugAll(buffer);

        //切换为写模式
        buffer.compact();
        debugAll(buffer);

        buffer.flip();
        debugAll(buffer);


    }
}
