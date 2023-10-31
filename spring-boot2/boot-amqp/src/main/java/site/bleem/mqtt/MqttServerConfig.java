//package site.bleem.mqtt;
//
//import cn.hutool.core.collection.ConcurrentHashSet;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.*;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import site.bleem.amqp.MessageHandler;
//
//import javax.annotation.Resource;
//
///**
// * @author yubangshui
// * @desc todo
// * @date 2023/8/31
// */
//@Order(2)
//@Slf4j
//@Configuration
//public class MqttServerConfig implements MqttCallback {
//
//    @Resource
//    private MqttProperties mqtt;
//
//
//    @Resource
//    private MqttClient mqttClient; // 发送消息的客户端
//
////
////    @Resource
////    private MqttMessageService mqttMessageService;
//
//    @Resource
//    private MessageHandler messageHandler;
//
//    private ConcurrentHashSet<String> concurrentHashSetTopic = new ConcurrentHashSet<>();
//
//
//    /**
//     * mqtt服务器连接
//     *
//     * @return mqttClient
//     * @throws MqttException 异常
//     */
//    @Bean
//    public MqttClient mqttClient() throws MqttException {
//        String clientId = mqtt.getClientId() + Math.random();
//        MqttClient mqttClient = new MqttClient(mqtt.getHost(), clientId, new MemoryPersistence());
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setCleanSession(false);
//        options.setUserName(mqtt.getUsername()); // 用户名
//        options.setPassword(mqtt.getPassword().toCharArray()); // 密码
////        options.setConnectionTimeout(connectionTimeout); // 设置超时时间
////        options.setKeepAliveInterval(keepAliveInterval); // 设置会话心跳时间
//        mqttClient.setCallback(this); // 接收到消息的回调函数
//        mqttClient.connect(options);
//        return mqttClient;
//    }
//
//
////    @PostConstruct
////    @ConditionalOnBean(MqttClient.class)
////    public void addHeartTopic() throws MqttException {
//////        mqttClient.subscribe(mqtt.getHeartTopic());
//////        concurrentHashSetTopic.add(mqtt.getHeartTopic());
////    }
//
//    /**
//     * 是否连接正常
//     *
//     * @return true正常 false不正常
//     */
//    public boolean isConnected() {
//        if (mqttClient == null || !mqttClient.isConnected()) {
//            return false;
//        }
//        return true;
//    }
//    public void reConnect() throws MqttException {
//        if (mqttClient != null && mqttClient.isConnected()){
//            log.info("mqtt连接正常");
//            return;
//        }
//        mqttClient = mqttClient();
//        log.info("mqtt重连成功");
//        concurrentHashSetTopic.stream().forEach(topic->{
//            try {
//                mqttClient.subscribe(topic);
//                log.info("mqtt重新订阅topic:{}",topic);
//            } catch (MqttException e) {
//                log.warn("失败订阅topic:"+topic,e);
//            }
//        });
//    }
//
//    /**
//     * 订阅topic
//     *
//     * @param dtu
//     * @throws MqttException
//     */
//    public void subscribeTopic(String dtu) throws MqttException {
//        String topic = mqtt.getTopicPrefix() + dtu;
//        if (concurrentHashSetTopic.contains(topic)) {
//            MqttTopic mqttTopic = mqttClient.getTopic(topic);
//            if (mqttTopic != null) {
//                return;
//            }
//        } else {
//            concurrentHashSetTopic.add(topic);
//        }
//        mqttClient.subscribe(topic);
//    }
//
//    public MqttTopic getMqttTopic(String dtu) {
//        MqttTopic topic = mqttClient.getTopic(mqtt.getTopicPrefix() + dtu);
//        return topic;
//    }
//
//    /**
//     * 连接丢失后，一般在这里面进行重连
//     *
//     * @param throwable
//     */
//    @Override
//    public void connectionLost(Throwable throwable) {
//        log.warn("MQTT连接断开，尝试重连",throwable);
//        try {
//            reConnect();
//        } catch (MqttException e) {
//            log.warn("MQTT重连失败",e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * subscribe后得到的消息会执行到这里面
//     *
//     * @param topic
//     * @param mqttMessage
//     * @throws Exception
//     */
//    @Override
//    public void messageArrived(String topic, MqttMessage mqttMessage) {
//        String message = new String(mqttMessage.getPayload());
//        try {
//            log.info("接收MQTT消息：topic = {}, Qos = {}, message = {}", topic, mqttMessage.getQos(), message);
//            // 判断是否是实时消息
//            messageHandler.handle(topic,message);
////            mqttMessageService.consumer(topic, message);
//        } catch (Exception e) {
//            // 此处可能因为收到的消息不合法，会造成JSON转化异常，若异常未捕获，会导致MQTT客户端掉线
//            log.info("接收MQTT消息：topic = {}, Qos = {}, message = {},处理异常:{}", topic, mqttMessage.getQos(), message, e);
//        }
//    }
//
//    /**
//     * 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用
//     *
//     * @param iMqttDeliveryToken
//     */
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//    }
//}
