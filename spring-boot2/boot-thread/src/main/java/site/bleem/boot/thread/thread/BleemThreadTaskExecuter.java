package site.bleem.boot.thread.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yubangshui
 * @desc todo
 *
 * @date 2023/8/9
 */
@Slf4j
public class BleemThreadTaskExecuter extends Thread {

    private String threadKey;

    public BleemThreadTaskExecuter(String threadKey) {
        super(threadKey);
        this.threadKey = threadKey;
    }

    ConcurrentLinkedQueue<String> taskQueue = new ConcurrentLinkedQueue<>();//任务队列
    //客户任务池
    ConcurrentHashMap<String, AtomicInteger> taskPoolMap = new ConcurrentHashMap<>();


    public synchronized void enqueue(String task, Integer count) {
        if (taskPoolMap.containsKey(task)) {
            if (count > 0) {
                AtomicInteger atomicInteger = taskPoolMap.get(task);
                int addedAndGet = atomicInteger.addAndGet(count);
                log.info(getName() + "线程"+task+"任务运行次数增加：" + count + " 当前最新：" + addedAndGet);
            } else {
                taskPoolMap.put(task, new AtomicInteger(count));
            }
            //重新排队，将新加入的任务置为队尾
            for (int i = 0; i <taskQueue.size(); i++) {
                String peeked = taskQueue.peek();
                if (!task.equals(peeked)){
                    reOffer();
                }else {
                    taskQueue.poll();
                }
            }
            taskQueue.offer(task);
        } else {
            if (taskQueue.size() > 10) {
                pollQueue();
            }
            log.info(getName() + "线程增加任务：" + task + " 运行次数：" + count);
            taskQueue.offer(task);
            taskPoolMap.put(task, new AtomicInteger(count));
        }
    }

    @Override
    public void run() {
        while (!taskQueue.isEmpty()) {
            String task = taskQueue.peek();
            AtomicInteger count = taskPoolMap.get(task);
            try {
                if (count == null || count.get() == 0) {
                    pollQueue();
                    continue;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程睡眠异常！", e);
                }
                int decremented = count.decrementAndGet();
                log.info(getName() + "线程运行" + task + "任务，剩余次数：" + decremented);
                reOffer();
            } catch (Exception e) {
                log.error(getName() + "线程运行" + task + "任务,出现异常:"+e.getMessage(),e);
            }
        }
        log.info(getName() + "线程运行完成退出！");
        BleemThreadTaskPoolManager.getInstance().remove(this.threadKey);
    }

    @Override
    public synchronized void start() {
        if (!this.isAlive()) {
            super.start();
        } else {
            log.info(getName() + "线程正在运行中ing");
        }
    }

    /**
     * 移出队列
     */
    public synchronized void pollQueue() {
        String polled = taskQueue.poll();
        taskPoolMap.remove(polled);
        log.info(getName() + "线程：" + polled + "任务移除队列");
    }

    /**
     * 重新排队
     */
    public synchronized void reOffer() {
        String polled = taskQueue.poll();
        taskQueue.offer(polled);
        log.info(getName() + "线程运行" + polled + "任务重新排队");
    }

}
