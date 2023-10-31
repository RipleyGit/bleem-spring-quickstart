package site.bleem.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.bleem.amqp.strategy.TopicStrategy;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@Slf4j
@Service
public class MessageHandlerContext implements MessageHandler {

    @Resource
    private Map<String, TopicStrategy> topicStrategy;

    @Override
    public void handle(String topic, String message) {
        TopicStrategy strategy = topicStrategy.get(topic);
        strategy.handleMessage(message);

    }

}
