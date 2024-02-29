package site.bleem.common.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonDelayQueue implements ApplicationRunner {

    private final static String RD_QUEUE_FLAG = "BLEEM_REDISSON_QUEUE";
    private RedissonClient redissonClient;
    private RDelayedQueue<String> delayQueue;
    private RBlockingQueue<String> blockingQueue;
    @Resource
    private RedisProperties redisProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        serverConfig.setPassword(redisProperties.getPassword());
        serverConfig.setDatabase(redisProperties.getDatabase());
        // 创建 Redisson 客户端
        redissonClient = Redisson.create(config);
        // 创建阻塞队列
        blockingQueue = redissonClient.getBlockingQueue(RD_QUEUE_FLAG);
        // 创建带有延时特性的队列
        delayQueue = redissonClient.getDelayedQueue(blockingQueue);
//消费者线程使用take方法从队列中取出元素，如果队列为空，则线程会阻塞等待
        new Thread(() -> {
            while (true) {
                try {
                    //调用take方法时，它会等待直到队列非空，然后取出队头元素并返回，同时将该元素从队列中移除。
                    //如果队列为空，take方法将一直阻塞，直到队列中有新的元素到达。
                    //在不阻塞的情况下查看队列头部的元素，可以使用peek方法。peek方法返回队列头部的元素但不将其从队列中删除。
                    String task = blockingQueue.take();
                    log.info("接收到延迟任务:{}", task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, RD_QUEUE_FLAG + "-Consumer").start();
        log.info("Redis延时队列初始化成功！");
    }

    public void offerTask(String task, long seconds) {
        log.info("添加延迟任务:{} 延迟时间:{}s", task, seconds);
        //添加任务，并通过设置延时时间来实现任务的延时执行。一旦到达指定的延时时间，任务将被移动到阻塞队列 blockingQueue 中，等待被消费。
        delayQueue.offer(task, seconds, TimeUnit.SECONDS);
    }

}