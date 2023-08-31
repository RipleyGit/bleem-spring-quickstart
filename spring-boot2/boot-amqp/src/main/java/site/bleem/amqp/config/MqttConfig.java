package site.bleem.amqp.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/31
 */
@Data
@Slf4j
//@ConfigurationProperties(prefix = "mqtt")
@Configuration
public class MqttConfig {


    @Value("${mqtt.host:tcp://172.22.1.136:1883}")
    private String host="tcp://172.22.1.136:1883";
    @Value("${mqtt.username:admin}")
    private String username="admin";
    @Value("${mqtt.password:root1234}")
    private String password="root1234";
    @Value("${mqtt.client-id:ai}")
    private String clientId="ai";
    @Value("${mqtt.topic-prefix:/vehicle/}")
    private String topicPrefix="/vehicle/";

    @Resource
    private MqttClient mqttClient; // 发送消息的客户端

    /**
     * mqtt服务器连接
     *
     * @return mqttClient
     * @throws MqttException 异常
     */
    @Bean
    public MqttClient mqttClient() throws MqttException {
        clientId = clientId + Math.random();
        MqttClient mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(username); // 用户名
        options.setPassword(password.toCharArray()); // 密码
//        options.setConnectionTimeout(connectionTimeout); // 设置超时时间
//        options.setKeepAliveInterval(keepAliveInterval); // 设置会话心跳时间
        mqttClient.setCallback(new ReplyMqttCallback()); // 接收到消息的回调函数
        mqttClient.connect(options);
        return mqttClient;
    }

    public void subscribeTopic(String dtu) throws MqttException {
        mqttClient.subscribe(topicPrefix+dtu);
    }

    public MqttTopic getMqttTopic(String dtu){
        MqttTopic topic = mqttClient.getTopic(topicPrefix + dtu);
        return topic;
    }

}
