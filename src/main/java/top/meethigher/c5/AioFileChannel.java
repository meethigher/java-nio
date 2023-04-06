package top.meethigher.c5;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class AioFileChannel {
    public static void main(String[] args) throws Exception {
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ);
        //参数1 存储结果的Buffer
        //参数2 读取的起始位置
        //参数3 附件，一次读不完，存储下一次的结果
        //参数4 回调对象
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);

        log.info("read start...");
        channel.read(byteBuffer, 0, ByteBuffer.allocate(4), new CompletionHandler<Integer, ByteBuffer>() {
            //read成功
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                log.info("read completed...");
                byteBuffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
            }

            //read出现异常
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
        log.info("read end...");
        //处理数据的是个守护线程
        //主线程结束，守护线程即使没执行完，也结束了，因此加个阻塞
        System.in.read();
    }
}
