package top.meethigher.c3.propro;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenchuancheng github.com/meethigher
 * @since 2023/3/28 21:22
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        ssc.register(boss, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //创建固定数量的worker
        //下面这种获取方式，像我的cpu是8核16线程，那么同一时刻可以跑16个线程，获取到的可用处理器数量就是16
        //但是针对于docker，我分配了1个处理器，他还是会拿到16个。在jdk10之后，可以使用jvm参数UseContainerSupport配置，方能正确获取到1个处理器
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger index = new AtomicInteger(0);
        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    //关联给worker的selector，监听read write
                    log.info("connected...{}", sc.getRemoteAddress());
                    log.info("before registered...{}", sc.getRemoteAddress());
                    //使用轮询算法
                    workers[index.getAndIncrement() % workers.length].register(sc);
                    log.info("after registered...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;

        private Selector selector;

        private String name;

        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        /**
         * 初始化线程和selector
         */
        public void register(SocketChannel sc) throws IOException {
            log.info("register");
            if (!start) {
                this.selector = Selector.open();
                this.start = true;
                this.thread = new Thread(this, name);
                this.thread.start();
            }
            //唤醒select
            selector.wakeup();
            sc.register(selector, SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        SocketChannel sc = (SocketChannel) key.channel();
                        if (key.isReadable()) {
                            log.info("read...{}", sc.getRemoteAddress());
                            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                            sc.read(byteBuffer);
                            byteBuffer.flip();
                            System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
