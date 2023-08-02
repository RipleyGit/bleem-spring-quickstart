package site.bleem.boot.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@ConditionalOnProperty(
    name = {"kafka.enable"}
)
@Service
public class ProducerService {
    private static final Logger log = LoggerFactory.getLogger(ProducerService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ListenableFuture<SendResult<String, Object>> sendMessage(String topic, Object o) {
        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(topic, JSONObject.toJSONBytes(o, new SerializerFeature[0]));
        future.addCallback((result) -> {
            log.info("生产者成功发送消息到topic:{} partition:{}的消息", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
        }, (ex) -> {
            log.error("生产者发送消失败，原因：{}", ex.getMessage());
        });
        return future;
    }

    public ListenableFuture<SendResult<String, Object>> sendMsgByKey(String topic, String key, Object o) {
        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(topic, key, JSONObject.toJSONBytes(o, new SerializerFeature[0]));
        future.addCallback((result) -> {
            log.info("生产者成功发送消息到topic:{} partition:{}的消息", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
        }, (ex) -> {
            log.error("生产者发送消失败，原因：{}", ex.getMessage());
        });
        return future;
    }

    public ListenableFuture<SendResult<String, Object>> sendMsgByKey(String topic, String key, byte[] o) {
        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(topic, key, o);
        future.addCallback((result) -> {
            log.info("生产者成功发送消息到topic:{} partition:{}的消息", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
        }, (ex) -> {
            log.error("生产者发送消失败，原因：{}", ex.getMessage());
        });
        return future;
    }

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
