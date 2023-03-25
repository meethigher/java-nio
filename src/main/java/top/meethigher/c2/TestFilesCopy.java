package top.meethigher.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class TestFilesCopy {
    public static void main(String[] args) throws Exception {
        String sourceStr = "D:\\开发手册";
        Path source = Paths.get(sourceStr);
        String targetStr = "C:\\Users\\meethigher\\Desktop\\web-filemanager";
        Files.walk(source).forEach(path -> {
            try {
                String replace = path.toString().replace(sourceStr, targetStr);
                System.out.println(replace);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(Paths.get(replace));
                } else {
                    Files.copy(path, Paths.get(replace), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {

            }
        });
    }
}
