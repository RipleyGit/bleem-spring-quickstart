package site.bleem.boot.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yubangshui
 * @desc 告警联动线程管理（单例）
 * @date 2023/7/26
 */
@Slf4j
public class AlarmLinkThreadManager {
    private static ConcurrentHashMap<String, AlarmLinkThreadExecutor> concurrentHashMap = new ConcurrentHashMap<>();

    private static Set<Long> taskIdSet = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private AlarmLinkThreadManager() {
    }

    private static class SingletonHolder {
        private static final AlarmLinkThreadManager INSTANCE = new AlarmLinkThreadManager();
    }

    public static AlarmLinkThreadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 线程结束，退出管理
     *
     * @param hid
     */
    public void remove(String hid) {
        synchronized (this) {
            if (getInstance().concurrentHashMap.containsKey(hid)) {
                concurrentHashMap.remove(hid);
            }
        }
    }

    public void status() {
        for (AlarmLinkThreadExecutor executor : getInstance().concurrentHashMap.values()) {
            if (executor.isAlive()) {
                log.debug("活跃线程：" + executor.getName());
                log.debug("消息队列：" + String.join(",", executor.getMessageQueue()));
//                log.info("消息任务：");
//                for (Map.Entry<String, ConcurrentSkipListMap<Long, Integer>> entry : executor.getConcurrentSkipListMap().entrySet()) {
//                    log.info("待播放消息：" + entry.getKey());
//                    for (Map.Entry<Long, Integer> subEntry : entry.getValue().entrySet()) {
//                        log.info("待运行任务：" + subEntry.getKey() + " 待播放数：" + subEntry.getValue());
//                    }
//                }
            } else {
                log.debug(executor.getName() + "非运行中：" + executor.getState().name());
            }
        }
    }

    public void active() {
        if (getInstance().concurrentHashMap.isEmpty()) {
            return;
        }
        for (AlarmLinkThreadExecutor threadExecutor : getInstance().concurrentHashMap.values()) {
            if (!threadExecutor.isAlive()) {
                threadExecutor.start();
            }
        }
    }

    public void removeOnSet(Long taskId) {
        synchronized (this) {
            if (taskIdSet.contains(taskId)) {
                taskIdSet.remove(taskId);
            }
        }
    }

    /**
     * 加入运行队列
     *
     * @param hid
     * @param message
     * @param taskId
     * @param times
     */
    public void enqueue(String hid, String message, Long taskId, Integer times) {
        if (getInstance().taskIdSet.contains(taskId)) {
            return;
        }
        getInstance().taskIdSet.add(taskId);
        if (getInstance().concurrentHashMap.containsKey(hid)) {
            AlarmLinkThreadExecutor executor = getInstance().concurrentHashMap.get(hid);
            executor.enqueue(message, taskId, times);
            if (!executor.isAlive()) {
                executor.start();
            }
            return;
        }
        AlarmLinkThreadExecutor executor = new AlarmLinkThreadExecutor(hid);
        executor.enqueue(message, taskId, times);
        executor.start();
        getInstance().concurrentHashMap.put(hid, executor);
    }
}
