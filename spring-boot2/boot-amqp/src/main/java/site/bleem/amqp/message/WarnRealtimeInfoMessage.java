package site.bleem.amqp.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@Slf4j
@Data
public class WarnRealtimeInfoMessage extends RealtimeInfoMessage {
    /**
     * 相机ID
     */
    private Integer cameraId;

    /**
     * 违规地点，对应t_camera摄像仪安装位置
     */
    private String position;

    /**
     * 用户设置的规则ID
     */
    private Integer ruleId;

    /**
     * 模型ID
     */
    private Integer modelId;

    /**
     * 是否电子封条告警 1代表是；0代表不是
     */
    private Integer alarmSeal;


    /**
     * 告警发生时间 yyyy-MM-dd HH:mm:ss
     */
    private String time;

    /**
     * 告警内容
     */
    private String alarmMsg;
    /**
     * 告警内容人数/车数
     */
    private String alarmMsgCount;
    /**
     * 告警ID
     */
    private Integer alarmMsgId;
    /**
     * 告警标题
     */
    private String alarmMsgTitle;
    /**
     * 告警图像
     */
    private String alarmImage;
    /**
     * 告警视频
     */
    private String alarmVideo;

    /**
     * 是否显示告警
     * 0时告警不入库，直接转发至第三方；1时入库、显示、转发
     */
    private Integer alarmShow;

    /**
     * 热成像摄像仪：温度值
     */
    private Integer temperature;

    @Override
    public void execute(){
        System.out.println("处理告警消息");
    }

}
