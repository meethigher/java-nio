package top.meethigher.c2;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPath {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Path source = Paths.get("1.txt"); // 相对路径 相当于使用 user.dir 环境变量来定位 1.txt
//        System.out.println(source.toAbsolutePath());
        source = Paths.get("d:\\1.txt"); // 绝对路径 代表了  d:\1.txt
        source = Paths.get("d:/1.txt"); // 绝对路径 同样代表了  d:\1.txt
        Path projects = Paths.get("d:\\data", "projects"); // 代表了  d:\data\projects

        source=Paths.get("C:\\Users\\meethigher\\Desktop\\netty\\java-nio\\src\\..\\target");
        System.out.println(source.toAbsolutePath());// C:\Users\meethigher\Desktop\netty\java-nio\src\..\target
        System.out.println(source.normalize());//正常化后路径 C:\Users\meethigher\Desktop\netty\java-nio\target
    }
}
