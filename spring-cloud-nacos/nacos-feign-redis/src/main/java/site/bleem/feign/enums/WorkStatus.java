package site.bleem.feign.enums;

public enum WorkStatus {
    FRR(0, "空闲状态", 0),
    AUDIO_PLAYING(1, "音频文件播放中", 1),
    MONITOR_UPING(2, "声波侦测上报中", 2),
    TALK_SENDING(3, "局部对讲发送中(局部按钮按下)", 2),
    TALK_RECIVEING(4, "局部对讲接收中", 2),
    TALK_EACHING(5, "局部双向对讲中", 2),
    DISPATCH_SENDING(6, "调度对讲发送中(调度按钮按下)", 2),
    DISPATCH_RECIVEING(7, "调度对讲接收中", 2),
    EISPATCH_EACHING(8, "调度双向对讲中", 2),
    SIP_CALLING(9, "SIP主动拨打电话中", 2),
    SIP_CALLEDING(10, "SIP被动接听电话中", 2),
    NO_USE11(11, "在线播放中", 1),
    INFO_SENDING(12, "信息发布(文本转语音)", 3),
    THIRD_ALARMING(13, "三方联动告警(文本转语音)", 4),
    NO_USE14(14, "预留", 0),
    URGENING(15, "紧急广播(连续语音流)", 5),
    UPGRADE(255, "固件升级状态", 8);

    private int code;
    private String desc;
    private int voiceStatus;

    public String getDesc() {
        return desc;
    }

    WorkStatus(int code, String desc, int voiceStatus) {
        this.code = code;
        this.desc = desc;
        this.voiceStatus = voiceStatus;
    }

    public static WorkStatus byCode(int code){
        for (WorkStatus status:WorkStatus.values()) {
            if (status.code == code){
                return status;
            }
        }
        return null;
    }
}