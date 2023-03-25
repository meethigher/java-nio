package top.meethigher.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TestFilesWalkFileTree {

    public static void classCount() throws IOException {
        AtomicInteger classCount = new AtomicInteger(0);
        //遍历文件，访问者模式
        Files.walkFileTree(Paths.get(System.getProperty("user.dir")), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().endsWith(".class")) {
                    log.info(file.toString());
                    classCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }

        });

        log.info("字节码数量：{}", classCount.get());
    }


    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("C:\\Users\\meethigher\\Desktop\\web-filemanager"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                log.info("==> 进入{}", dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                log.info("删除文件：{}", file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                log.info("<== 退出{}", dir);
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
