package top.meethigher.cas;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo03 {

    /**
     * 总访问量
     */
    private volatile static int count = 0;

    private static void request() throws InterruptedException {
        //模拟耗时5毫秒
        TimeUnit.MILLISECONDS.sleep(5);
        int expect;
        while (!compareAndSwap((expect = getCount()), expect + 1)) {
        }
    }

    private static int getCount() {
        return count;
    }

    /**
     * @param expect   期望值
     * @param newValue 新值
     * @return 成功true，失败false
     */
    private static synchronized boolean compareAndSwap(int expect, int newValue) {
        if (getCount() == expect) {
            count = newValue;
            return true;
        } else {
            return false;
        }
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