package top.meethigher.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j
public class ABADemo02 {

    private final static AtomicStampedReference<Integer> a = new AtomicStampedReference<>(1, 1);

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            log.info("初始值：{}", a.getReference());
            try {
                int expectNum = a.getReference();
                int newNum = expectNum + 1;
                int expectStamp = a.getStamp();
                int newStamp = expectStamp + 1;
                TimeUnit.SECONDS.sleep(1);
                //内部就是使用了cas
                boolean b = a.compareAndSet(expectNum, newNum, expectStamp, newStamp);
                log.info("cas操作：{}, 最终值：{}", b, a.getReference());
            } catch (Exception ignore) {
            }
        }, "主线程").start();
        new Thread(() -> {
            try {
                //让主线程先获取值
                TimeUnit.MILLISECONDS.sleep(20);
                //开始搞事！++a
                a.compareAndSet(a.getReference(), a.getReference() + 1, a.getStamp(), a.getStamp() + 1);
                log.info("进行++a操作，值：{}", a.getReference());
                //--a
                a.compareAndSet(a.getReference(), a.getReference() - 1, a.getStamp(), a.getStamp() + 1);
                log.info("进行--a操作，值：{}", a.getReference());
            } catch (Exception ignore) {

            }
        }, "干扰线程").start();
        System.in.read();
    }
}
