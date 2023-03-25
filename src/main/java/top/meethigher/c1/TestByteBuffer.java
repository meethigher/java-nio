package top.meethigher.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author chenchuancheng github.com/meethigher
 * @since 2023/3/15 00:06
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        /**
         * 获取FileChannel
         * 1. 通过输入输出流
         * 2. 通过RandomAccessFile
         */
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {//data.txt里面有13个字符
            //准备缓冲区10个字节
            ByteBuffer buffer = ByteBuffer.allocate(10);
            //要分多次读取，因为文件可能很大，但是缓冲区不能无限大
            while (true) {
                //从channel读取数据,向buffer写入
                int len = channel.read(buffer);
                log.info("读取到的字节数：{}",len);
                if (len == 0 || len == -1) {
                    break;
                }
                //打印buffer内容
                buffer.flip();//切换成读模式
                while (buffer.hasRemaining()) {//检查是否有剩余未读数据
                    byte b = buffer.get();//一次读一个字节
                    System.out.println((char) b);//转为字符打印
                }
                buffer.clear();//切换为写模式
            }
        } catch (IOException e) {
        }
    }
}
