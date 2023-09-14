package site.bleem.redis.config;

import lombok.extern.slf4j.Slf4j;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonDelayQueue implements ApplicationRunner {

    private final static String RD_QUEUE_FLAG="BW_WVP";
    private RedissonClient redissonClient;
    private RDelayedQueue<String> delayQueue;
    private RBlockingQueue<String> blockingQueue;
    @Resource
    private RedisProperties redisProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort());
//        serverConfig.setTimeout(redisProperties.getTimeout().);
        serverConfig.setPassword(redisProperties.getPassword());
        serverConfig.setDatabase(redisProperties.getDatabase());
        redissonClient = Redisson.create(config);
        blockingQueue = redissonClient.getBlockingQueue(RD_QUEUE_FLAG);
        delayQueue = redissonClient.getDelayedQueue(blockingQueue);

        new Thread(() -> {
            while (true) {
                try {
                    String task = blockingQueue.take();
                    log.info("接收到延迟任务:{}", task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, RD_QUEUE_FLAG+"-Consumer").start();
        log.info("Redis延时队列初始化成功！");
    }

    public void offerTask(String task, long seconds) {
        log.info("添加延迟任务:{} 延迟时间:{}s", task, seconds);
        delayQueue.offer(task, seconds, TimeUnit.SECONDS);
    }

}