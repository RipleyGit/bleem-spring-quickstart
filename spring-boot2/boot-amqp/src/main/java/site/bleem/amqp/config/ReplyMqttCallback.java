package site.bleem.amqp.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发布消息的回调类
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 */
@Slf4j
@Component
public class ReplyMqttCallback implements MqttCallback {

    @Resource
    private MqttConfig mqttConfig;

    public ReplyMqttCallback() {
    }

    /**
     * 连接丢失后，一般在这里面进行重连
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("MQTT连接断开，尝试重连",throwable);
        try {
            mqttConfig.reConnect();
        } catch (MqttException e) {
            log.warn("MQTT重连失败",e);
            throw new RuntimeException(e);
        }
    }



    /**
     * subscribe后得到的消息会执行到这里面
     *
     * @param topic
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        try {
            log.info("接收MQTT消息：topic = {}, Qos = {}, message = {}", topic, mqttMessage.getQos(), new String(mqttMessage.getPayload()));
            // 判断是否是实时消息
        } catch (Exception e) {
            // 此处可能因为收到的消息不合法，会造成JSON转化异常，若异常未捕获，会导致MQTT客户端掉线
            log.info("接收MQTT消息：topic = {}, Qos = {}, message = {},处理异常:{}", topic, mqttMessage.getQos(), new String(mqttMessage.getPayload()), e);
        }
    }

    /**
     * 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
