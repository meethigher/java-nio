package top.meethigher.c1;

import java.nio.ByteBuffer;

public class TestByteBufferAllocate {

    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(10).getClass());//java.nio.HeapByteBuffer java堆内存
        System.out.println(ByteBuffer.allocateDirect(10).getClass());//java.nio.DirectByteBuffer 直接内存
        /**
         * java堆内存： 读写效率较低，受到GC的影响(其他普通对象也是一样，在堆内存中。在GC时，内存分配地址会进行变更也就是进行了拷贝)
         * 直接内存：读写效率高(少一次数据拷贝)，不会受GC的影响(使用的系统内存，缺点是需要调用系统函数，分配的会比较慢，使用不当会造成内存泄漏)。
         */
    }
}
