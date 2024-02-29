package site.bleem.boot.web.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
public class LoadingCacheUtilTest {

    //基于容量的清除策略
    private static final Cache<String, String> CAPACITY_CACHE = CacheBuilder.newBuilder()
            // 设置缓存容量数
            .maximumSize(2)
            .build();

    /**
     * 当我们把缓存最大数设置成2时，意为当前cache的key最多存2个，超过两个会把最开始添加的那一个去掉，所以我们第一个key获取值为null。
     */
    @Test
    public void CAPACITY_CACHE_TEST() {
        CAPACITY_CACHE.put("1","a1");
        CAPACITY_CACHE.put("2","a2");
        CAPACITY_CACHE.put("3","a3");
        System.out.println(CAPACITY_CACHE.getIfPresent("1"));
        System.out.println(CAPACITY_CACHE.getIfPresent("2"));
        System.out.println(CAPACITY_CACHE.getIfPresent("3"));
    }

    // 写入缓存后多久过期
    private static final Cache<String, String> BEFORE_TIME_CACHE = CacheBuilder.newBuilder()
            // 写缓存后多久过期
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build();

    /**
     * 当我们把时间设置为写入缓存3秒后到期，可以看到当key为1的值在两秒后是可以获取到的，但在四秒后就获取不到了，因为这个key的有效期只有三秒。
     * expireAfterWrite是写入多久并且没有再次更新当前key的值，那么值就会失效。
     * @throws InterruptedException
     */
    @Test
    public  void beforeTimeCacheTest() throws InterruptedException {
        BEFORE_TIME_CACHE.put("1","a1");
        // 睡2秒
        Thread.sleep(2 * 1000);
        System.out.println(BEFORE_TIME_CACHE.getIfPresent("1"));
        // 睡2秒
        Thread.sleep(2 * 1000);
        System.out.println(BEFORE_TIME_CACHE.getIfPresent("1"));
    }

    //读写缓存后多久到期
    private static final Cache<String, String> AFTER_TIME_CACHE = CacheBuilder.newBuilder()
            // 读写缓存后多久过期
            .expireAfterAccess(3, TimeUnit.SECONDS)
            .build();

    /**
     * 每隔两秒获取是可以获取到值，但是隔四秒去获取就获取不到了
     *
     * 由此我们可以得出结论，expireAfterAccess是多久没有访问当前key，那么这个key的值就会在设置的时间后过期。
     * @throws InterruptedException
     */
    @Test
    public  void AFTER_TIME_CACHE_TEST() throws InterruptedException {
        AFTER_TIME_CACHE.put("1","a1");
        // 睡2秒
        Thread.sleep(2 * 1000);
        System.out.println(AFTER_TIME_CACHE.getIfPresent("1"));
        // 睡2秒
        Thread.sleep(2 * 1000);
        System.out.println(AFTER_TIME_CACHE.getIfPresent("1"));
        // 睡4秒
        Thread.sleep(4 * 1000);
        System.out.println(AFTER_TIME_CACHE.getIfPresent("1"));
    }

    //手动清除
    private static final Cache<String, String> MANUAL_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .build();


    @Test
    public void MANUAL_CACHE_TEST() throws InterruptedException {
        MANUAL_CACHE.put("1", "2");
        MANUAL_CACHE.put("2", "1");
        MANUAL_CACHE.put("3", "3");
        System.out.println(MANUAL_CACHE.getIfPresent("1"));
        System.out.println(MANUAL_CACHE.getIfPresent("2"));
        System.out.println(MANUAL_CACHE.getIfPresent("3"));
        // 清除单个
        MANUAL_CACHE.invalidate("1");
        System.out.println(MANUAL_CACHE.getIfPresent("1"));
        // 清除多个
//        MANUAL_CACHE.invalidateAll();
        ArrayList<String> list = Lists.newArrayList("2", "3");
        MANUAL_CACHE.invalidateAll(list);
        System.out.println(MANUAL_CACHE.getIfPresent("2"));
        System.out.println(MANUAL_CACHE.getIfPresent("3"));
    }
}