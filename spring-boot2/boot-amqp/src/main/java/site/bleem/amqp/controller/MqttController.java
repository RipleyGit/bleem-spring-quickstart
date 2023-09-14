package site.bleem.amqp.controller;

import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.web.bind.annotation.*;
import site.bleem.amqp.config.MqttConfig;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/31
 */
@RestController
public class MqttController {

    @Resource
    private MqttConfig mqttConfig;

    @GetMapping(value = "/subscribe/{topoic}")
    @ResponseBody
    public void subscribeTopic(@PathVariable("topoic") String topoic) throws MqttException {
        mqttConfig.subscribeTopic(topoic);
    }

    @GetMapping(value = "/publish/{topoic}")
    @ResponseBody
    public void publishTopic(@PathVariable("topoic") String topoic) throws Exception {
        MqttTopic mqttTopic = mqttConfig.getMqttTopic(topoic);
        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        String msg ="{\"dtu\":\"11111\",\"id\":\"9d2c017c-7b27-4b33-b98e-85b08bd8d12c\",\"msgType\":\"videoRoad\",\"road\":\"1\",\"videoUrl\":\"rtmp://192.168.108.234:1935/live/11111-1\"}";
        message.setPayload(msg.getBytes("UTF-8"));
        MqttDeliveryToken token = mqttTopic.publish(message);
        token.waitForCompletion();
    }


}
