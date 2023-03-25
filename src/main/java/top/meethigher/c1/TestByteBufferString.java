package top.meethigher.c1;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

public class TestByteBufferString {
    public static void main(String[] args) {
        //字符串转为ByteBuffer

        //方法一：字节数组，写模式
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello world".getBytes(StandardCharsets.UTF_8));
        debugAll(buffer);
        System.out.println((char) buffer.get());


        //方法二：直接通过编码转，读模式
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello world");
//        ByteBuffer buffer1 = Charset.forName("UTF-8").encode("hello world");
        debugAll(buffer1);
//        buffer1.flip();//查看TestByteBufferFlip源码
        System.out.println((char) buffer1.get());

        //方法三: wrap，读模式
        ByteBuffer wrap = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));
        debugAll(wrap);
        System.out.println((char) wrap.get());

        debugAll(wrap);
        //ByteBuffer转为字符串

        //方法一：使用编码
        wrap.rewind();
        String s = StandardCharsets.UTF_8.decode(wrap).toString();
        System.out.println(s);

        //方法二：使用字节数组
        wrap.rewind();
        String s1 = new String(wrap.array(), StandardCharsets.UTF_8);
        System.out.println(s1);
    }
}
