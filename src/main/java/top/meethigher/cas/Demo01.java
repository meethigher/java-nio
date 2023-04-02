package top.meethigher.cas;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 统计用户访问量。用户每发一次请求，访问量+1。
 * <p>
 * 需要模拟100人同时访问，每个人进行10次请求。最后总访问次数应该是1000次。
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/3/30 20:52
 */
public class Demo01 {

    /**
     * 总访问量
     */
    //private static AtomicInteger count = new AtomicInteger(0);
    private static int count = 0;

    private static void request() throws InterruptedException {
        //模拟耗时5毫秒
        TimeUnit.MILLISECONDS.sleep(5);
        //count.getAndIncrement();
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int threadSize = 100;

        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                //模拟用户行为：访问10次网站
                for (int j = 0; j < 10; j++) {
                    try {
                        request();
                    } catch (InterruptedException ignore) {
                    }
                }
                countDownLatch.countDown();
            }).start();
        }

        //等待所有模拟用户执行完
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        long d = endTime - startTime;
        System.out.println(Thread.currentThread().getName() + ", 耗时" + d + "毫秒, 总访问量" + count);

    }


}
