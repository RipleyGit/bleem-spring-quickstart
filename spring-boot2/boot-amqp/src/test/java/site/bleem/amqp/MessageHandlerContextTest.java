package site.bleem.amqp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.bleem.AmqpApplication;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmqpApplication.class)
public class MessageHandlerContextTest {
    @Resource
    private MessageHandler messageHandler;

//    @MockBean
//    private MqttServerConfig mqttServerConfig;

    @Test
    public void handleTest(){
        String realtimeInfoTopic = "realtimeInfo";
        //告警消息
        String realtimeInfoMessage = "{\"realtimeInfoId\":0,\"cameraId\":12,\"modelId\":11,\"ruleId\":1,\"time\":\"2021-05-12 16:46:32\",\"alarmMsg\":\"\",\"alarmMsgId\":120,\"temperature\":100,\"alarmShow\":1,\"alarmSeal\":0,\"alarmImage\":\"http://172.21.34.191:8080/savingImages/20210512/12-1-2021-05-12-16-46-32-455.jpg\",\"alarmVideo\":\"http://172.21.34.191:8080/savingVideos/20210512/12-1-2021-05-12-16-46-35-354.mp4\"}";
        messageHandler.handle(realtimeInfoTopic,realtimeInfoMessage);
        //摄像机状态消息
        realtimeInfoMessage = "{\"realtimeInfoId\":1,\"cameraStates\":[{\"cameraId\":0,\"cameraState\":0},{\"cameraId\":1,\"cameraState\":1}]}";
        messageHandler.handle(realtimeInfoTopic,realtimeInfoMessage);
        //磁盘空间状态示例
        realtimeInfoMessage = "{\"realtimeInfoId\":2,\"total\":400,\"free\":50,\"alarm\":1}";
        messageHandler.handle(realtimeInfoTopic,realtimeInfoMessage);
        //服务器状态示例
        realtimeInfoMessage = "{\"realtimeInfoId\":3,\"serverInfo\":[{\"type\":\"主服务器\",\"ip\":\"172.21.34.192\",\"port\":1883,\"online\":1},{\"type\":\"子服务器\",\"ip\":\"172.21.34.193\",\"port\":1883,\"online\":1}]}";
        messageHandler.handle(realtimeInfoTopic,realtimeInfoMessage);

//        String realtimeInfoTopic = "realtimeInfo";
//        String realtimeInfoMessage = "{\"realtimeInfoId\":0,\"cameraId\":12,\"modelId\":11,\"ruleId\":1,\"time\":\"2021-05-12 16:46:32\",\"alarmMsg\":\"\",\"alarmMsgId\":120,\"temperature\":100,\"alarmShow\":1,\"alarmSeal\":0,\"alarmImage\":\"http://172.21.34.191:8080/savingImages/20210512/12-1-2021-05-12-16-46-32-455.jpg\",\"alarmVideo\":\"http://172.21.34.191:8080/savingVideos/20210512/12-1-2021-05-12-16-46-35-354.mp4\"}";
//        messageHandler.handle(realtimeInfoTopic,realtimeInfoMessage);

    }
}