package site.bleem.amqp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/9/1
 */
@Data
@ConfigurationProperties(prefix = "spring.mqtt", ignoreInvalidFields = true)
@Order(0)
@Component
public class MqttProperties {

    private String host;
    private String username;
    private String password;
    private String clientId;
    private String topicPrefix;
}
