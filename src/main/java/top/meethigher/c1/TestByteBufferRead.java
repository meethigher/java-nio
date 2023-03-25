package top.meethigher.c1;

import java.nio.ByteBuffer;

import static top.meethigher.c1.ByteBufferUtil.debugAll;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();//读模式


        buffer.get(new byte[4]);
        debugAll(buffer);

        //从头开始读
        buffer.rewind();
        buffer.get();
        debugAll(buffer);

        /**
         * mark: 做一个标记，记录position位置
         * reset: 重置position到mark
         */
        buffer.mark();
        for (int i = 0; i < 5; i++) {
            System.out.println((char) buffer.get());
            buffer.reset();
        }
        debugAll(buffer);
        buffer.get();
        debugAll(buffer);
        buffer.reset();
        buffer.get(2);
        debugAll(buffer);
    }
}
