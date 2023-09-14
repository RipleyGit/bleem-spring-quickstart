package site.bleem.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 过期监听事件
 * @author 程就人生
 * @Date
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
  
  private static final Logger log = LoggerFactory.getLogger(RedisKeyExpirationListener.class);

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
      // 获取过期的keyString body = new String(message.getBody());
        String keyName = new String(message.getChannel());
        System.out.printf("body"+"key"+keyName);
        String dddd  = new String(message.getBody());
        System.out.printf("key已过期，当前时间："+System.currentTimeMillis());
//        // 对过期的key进行分割，获取过期时间
//        LocalDateTime parse = LocalDateTime.parse(keyName.split("@")[1], dateTimeFormatter);
//        // 计算出过期时长，当前时间-过期时间
//        long seconds = Duration.between(parse, LocalDateTime.now()).getSeconds();
//        // 打印真正过期时间，展示过期之后时间
//        redisTemplate.execute((RedisCallback<Object>) connection -> {
//            log.info("过期key:" + keyName + " ,滞后时间" + seconds);
//            return null;
//        });
    }
}