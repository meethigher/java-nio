package top.meethigher.c3;

import java.io.IOException;
import java.nio.channels.Selector;

public class BugTest {

    public static void main(String[] args) throws Exception {
        BugObj obj = new BugObj();
        System.in.read();
    }

    static class BugObj implements Runnable {

        private Thread thread;

        private volatile Object object;

        public BugObj() throws IOException {
            System.out.println(this);
            this.thread = new Thread(this);
            this.thread.start();
            this.object = Selector.open();
            System.out.println(this);
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(this.object);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
