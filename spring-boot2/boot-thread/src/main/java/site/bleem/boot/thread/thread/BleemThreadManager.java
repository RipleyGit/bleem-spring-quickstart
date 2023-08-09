package site.bleem.boot.thread.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/9
 */
@Slf4j
public class BleemThreadManager {
    private static ConcurrentHashMap<String, BleemThreadExecuter> concurrentHashMap = new ConcurrentHashMap<>();
    private static class SingletonHolder {
        private static final BleemThreadManager INSTANCE = new BleemThreadManager();
    }

    /**
     * 线程结束，退出管理
     *
     * @param hid
     */
    public void remove(String hid) {
        synchronized (this) {
            if (getInstance().concurrentHashMap.containsKey(hid)) {
                log.info(hid + "线程执行完成退出");
                concurrentHashMap.remove(hid);
            }
        }
    }

    public static BleemThreadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 加入运行队列
     * @param key
     * @param count
     */
    public void enqueue(String key, Integer count) {
        if (getInstance().concurrentHashMap.containsKey(key)) {
            log.info(key+"线程已存在，加入运行队列");
            BleemThreadExecuter executor = getInstance().concurrentHashMap.get(key);
            executor.enqueue(count);
            return;
        }
        log.info("创建线程："+key);
        BleemThreadExecuter executor = new BleemThreadExecuter(key);
        executor.enqueue(count);
        if (getInstance().concurrentHashMap.containsKey(key)){
            log.info("加入过程中线程已被创建："+key);
            enqueue(key,count);
            return;
        }
        log.info("线程："+executor.getName()+"加入管理");
        getInstance().concurrentHashMap.put(key, executor);
    }


    public void activation() {
        synchronized (this) {
            if (getInstance().concurrentHashMap.isEmpty()) {
                return;
            }
            getInstance().concurrentHashMap.values().parallelStream().forEach(executor -> {
                try {
                    executor.start();
                } catch (Exception e) {
                    log.error(executor.getName() + "线程程启动异常", e);
                }
            });
        }
    }

}
