package site.bleem.amqp.enums;

/**
 * MQTT消息类型
 */
public enum MqttMsgType {
    VIDEO_ROAD("videoRoad","开启推流"),
    videoClose("videoClose","关闭推流"),
    heartBeat("heartBeat","心跳指令");

    private String cmd;
    private String cmdDesc;

    MqttMsgType(String cmd, String cmdDesc) {
        this.cmd = cmd;
        this.cmdDesc = cmdDesc;
    }
}
