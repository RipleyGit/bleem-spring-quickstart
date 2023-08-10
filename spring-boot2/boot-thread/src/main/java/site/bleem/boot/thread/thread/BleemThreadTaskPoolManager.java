package site.bleem.boot.thread.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/9
 */
@Slf4j
public class BleemThreadTaskPoolManager {
    private static ConcurrentHashMap<String, BleemThreadTaskExecuter> threadTaskPoolMap = new ConcurrentHashMap<>();
    private static class SingletonHolder {
        private static final BleemThreadTaskPoolManager INSTANCE = new BleemThreadTaskPoolManager();
    }

    /**
     * 线程结束，退出管理
     *
     * @param hid
     */
    public void remove(String hid) {
        synchronized (this) {
            if (getInstance().threadTaskPoolMap.containsKey(hid)) {
                log.info(hid + "线程执行完成退出");
                threadTaskPoolMap.remove(hid);
            }
        }
    }

    public static BleemThreadTaskPoolManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 加入运行队列
     *
     * @param key
     * @param user
     * @param count
     */
    public void enqueue(String task, String user, Integer count) {
        synchronized (this) {
            if (getInstance().threadTaskPoolMap.containsKey(user)) {
                log.info(user+"线程已存在，加入运行队列");
                BleemThreadTaskExecuter executor = getInstance().threadTaskPoolMap.get(user);
                executor.enqueue(task,count);
                return;
            }
            log.info("创建线程："+user);
            BleemThreadTaskExecuter executor = new BleemThreadTaskExecuter(user);
            executor.enqueue(task, count);
            log.info("线程："+executor.getName()+"加入管理");
            getInstance().threadTaskPoolMap.put(user, executor);
        }
    }


    public void activation() {
        synchronized (this) {
            if (getInstance().threadTaskPoolMap.isEmpty()) {
                return;
            }
            getInstance().threadTaskPoolMap.values().parallelStream().forEach(executor -> {
                try {
                    executor.start();
                } catch (Exception e) {
                    log.error(executor.getName() + "线程程启动异常", e);
                }
            });
        }
    }

}
