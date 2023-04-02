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
        Worker worker = new Worker("worker-0");
        worker.register();
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
                    sc.register(worker.worker, SelectionKey.OP_READ + SelectionKey.OP_WRITE);
                    log.info("after registered...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;

        private Selector worker;

        private String name;

        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        /**
         * 初始化线程和selector
         */
        public void register() throws IOException {
            log.error("register");
            if (!start) {
                this.worker = Selector.open();
                this.start = true;
                this.thread = new Thread(this, name);
                this.thread.start();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.worker.select();
                    Iterator<SelectionKey> iterator = this.worker.selectedKeys().iterator();
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
