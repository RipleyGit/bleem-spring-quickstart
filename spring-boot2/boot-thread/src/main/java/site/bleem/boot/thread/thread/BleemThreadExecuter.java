package site.bleem.boot.thread.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/9
 */
@Slf4j
public class BleemThreadExecuter extends Thread {

    private String threadKey;

    public BleemThreadExecuter(String threadKey) {
        super();
        this.threadKey = threadKey;
    }

    private AtomicInteger atomicInteger = new AtomicInteger();

    public void enqueue(Integer count) {
        int addedAndGet = atomicInteger.addAndGet(count);
        log.info(getName() + "线程中值增加：" + count + " 当前最新：" + addedAndGet);
    }

    @Override
    public void run() {
        while (atomicInteger.get() != 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常！",e);
            }
            int decremented = atomicInteger.decrementAndGet();
            log.info(getName() + "线程中值减1 当前最新：" + decremented);
        }
        log.info(getName() + "线程运行完成退出！");
        BleemThreadManager.getInstance().remove(this.threadKey);
    }

    @Override
    public synchronized void start() {
        if (!this.isAlive()) {
            super.start();
        }else {
            log.info(getName() + "线程正在运行中ing");
        }
    }
}
