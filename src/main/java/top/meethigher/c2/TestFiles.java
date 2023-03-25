package top.meethigher.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class TestFiles {
    public static void main(String[] args) throws IOException {
        log.info("start");
        Path source = Paths.get("B:\\cepheus_images_V11.0.9.0.QFACNXM_20200408.0000.00_10.0_cn_c587631ef4.tgz");
        Path target = Paths.get("C:\\Users\\meethigher\\Desktop\\to.tgz");
        Files.copy(source,
                target,
                StandardCopyOption.REPLACE_EXISTING);
        log.info("end");
    }
}
