package site.bleem.boot.web.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LocalCacheManager implements ILocalCache {
    private static Cache<String, Integer> guavaCache = null;
    private static LocalCacheManager localCacheManager = null;
    private static final Lock reentrantLock = new ReentrantLock();

    private LocalCacheManager() {
        initCache();
    }

    public static LocalCacheManager getInstance() {
        if (null == localCacheManager) {
            reentrantLock.lock();
            log.debug("（线程安全，双重检测,可重入锁，高效）");
            try {
                if (null == localCacheManager) {
                    localCacheManager = new LocalCacheManager();
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return localCacheManager;
    }

    private void initCache() {
        log.debug("初始化缓存");
        guavaCache = CacheBuilder.newBuilder().build();
    }

    @Override
    public void put(String key, Integer value) {
        guavaCache.put(key, value);
    }

    @Override
    public Integer get(String key) {
        return guavaCache.getIfPresent(key);
    }
}