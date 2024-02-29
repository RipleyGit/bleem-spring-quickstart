package site.bleem.boot.hazelcast.config;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class HystrixContext {

    private HystrixRequestContext hystrixRequestContext;

    private HystrixContext(){
        hystrixRequestContext = HystrixRequestContext.initializeContext();
    }

    private static class LazyHolder {
        private static final HystrixContext INSTANCE = new HystrixContext();
    }

    public static final HystrixContext getInstance() {
        return LazyHolder.INSTANCE;
    }
}
