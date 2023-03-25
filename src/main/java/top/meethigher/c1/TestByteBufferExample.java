package top.meethigher.c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

public class TestByteBufferExample {

    public static void main(String[] args) {
        /**
         * 网络上有多条数据发送给服务端，数据之间使用 `\n` 进行分隔，但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
         *
         * Hello,world\n
         * I'm zhangsan\n
         * How are you?\n
         *
         * 变成了下面的两个 byteBuffer (粘包、半包)
         *
         * Hello,world\nI'm zhangsan\nHo
         * w are you?\n
         */
        //用来模拟服务端接收到的消息
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes(StandardCharsets.UTF_8));
        handle(source);
        source.put("w are you?\n".getBytes(StandardCharsets.UTF_8));
        handle(source);
    }

    private static void handle(ByteBuffer buffer) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            //找到一条完整消息
            if (buffer.get(i) == '\n') {
                int length = i + 1 - buffer.position();
                //把这条消息存入新的ByteBuffer
                ByteBuffer byteBuffer = ByteBuffer.allocate(length);
                //从buffer读，像byteBuffer写
                for (int j = 0; j < length; j++) {
                    byteBuffer.put(buffer.get());
                }
                byteBuffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
            }
        }
        //移动到未读的位置
        buffer.compact();
    }
}
