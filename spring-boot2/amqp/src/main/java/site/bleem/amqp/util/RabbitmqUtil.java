package site.bleem.amqp.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqUtil {
    private static final Logger log = LoggerFactory.getLogger(RabbitmqUtil.class);
    @Autowired
    RabbitTemplate rabbitTemplate;

    public RabbitmqUtil() {
    }

    public <T> void sendMsqToQueue(String queueName, T value) {
        String msg = JSON.toJSONString(value);
        log.info("------->{}", msg);
        this.rabbitTemplate.convertAndSend(queueName, msg);
    }

    public <T> void sendMsgToExchange(String exchange, String routingKey, T value) {
        String msg = JSON.toJSONString(value);
        log.info("------->ex:{} routing:{}  {}", new Object[]{exchange, routingKey, msg});
        this.rabbitTemplate.convertAndSend(exchange, routingKey, msg);
    }

    public <T> void sendMsqToQueue(String queueName, T value, int ttl) {
        String msg = JSON.toJSONString(value);
        if (ttl > 0) {
            this.rabbitTemplate.convertAndSend(queueName, msg, (message) -> {
                message.getMessageProperties().setExpiration(String.valueOf(ttl));
                return message;
            });
        } else {
            this.rabbitTemplate.convertAndSend(queueName, msg);
        }

    }

    public <T> void sendMsqToExchange(String exchangeName, T value) {
        String msg = JSON.toJSONString(value);
        this.rabbitTemplate.convertAndSend(exchangeName, "", msg);
    }
}
