package top.meethigher.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ABADemo {

    private final static AtomicInteger a = new AtomicInteger(1);

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            log.info("初始值：{}", a.get());
            try {
                int expectNum = a.get();
                int newNum = expectNum + 1;
                TimeUnit.SECONDS.sleep(1);
                //内部就是使用了cas
                boolean b = a.compareAndSet(expectNum, newNum);
                log.info("cas操作：{}, 最终值：{}", b, a.get());
            } catch (Exception ignore) {
            }
        }, "主线程").start();
        new Thread(() -> {
            try {
                //让主线程先获取值
                TimeUnit.MILLISECONDS.sleep(20);
                //开始搞事！++a
                a.incrementAndGet();
                log.info("进行++a操作，值：{}", a.get());
                //--a
                a.decrementAndGet();
                log.info("进行--a操作，值：{}", a.get());
            } catch (Exception ignore) {

            }
        }, "干扰线程").start();
        System.in.read();
    }
}
