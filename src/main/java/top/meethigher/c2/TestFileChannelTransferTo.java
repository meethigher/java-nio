package top.meethigher.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Slf4j
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        log.info("start");
        String from = "B:\\cepheus_images_V11.0.9.0.QFACNXM_20200408.0000.00_10.0_cn_c587631ef4.tgz";
        String to = "C:\\Users\\meethigher\\Desktop\\to.tgz";
        try (FileChannel fromChannel = new FileInputStream(from).getChannel();
             FileChannel toChannel = new FileOutputStream(to).getChannel()) {
            //效率比文件输入输出流效率要高。只要jdk中带transferTo的，都采用了操作系统的零拷贝技术
            //缺点：一次只能传输2g的数据
            //下面是一次性传输大于2g的数据示例
            long left = fromChannel.size();
            log.info("file size: {} B", left);
            while (left > 0) {
                // idea快捷键 ctrl+alt+v,可以快速将变量提取出来
                long realTransfer = fromChannel.transferTo(fromChannel.size() - left, left, toChannel);
                log.info("real transfer: {} B", realTransfer);
                left -= realTransfer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("end");
    }
}
