package site.bleem.amqp.strategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.springframework.stereotype.Component;
import site.bleem.amqp.message.RealtimeInfoMessage;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@Component("realtimeInfo")
public class RealtimeInfoTopicStrategy implements TopicStrategy {

    public void handleMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            RealtimeInfoMessage realtimeInfoMessage = mapper.readValue(message, RealtimeInfoMessage.class);
            realtimeInfoMessage.execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
