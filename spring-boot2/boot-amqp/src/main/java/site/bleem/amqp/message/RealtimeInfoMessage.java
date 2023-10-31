package site.bleem.amqp.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "realtimeInfoId")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WarnRealtimeInfoMessage.class, name = "0"),
        @JsonSubTypes.Type(value = CameraInfoMessage.class, name = "1"),
        @JsonSubTypes.Type(value = DiskInfoMessage.class, name = "2"),
        @JsonSubTypes.Type(value = ServerInfoMessage.class, name = "3")
})
@Data
public abstract class RealtimeInfoMessage {
//    realtimeInfo
//            含义
//0
//    报警
//1
//    所有摄像仪状态
//2
//    磁盘状态
//3
//    服务器状态
    private Integer realtimeInfoId;

    private String time;

    public void execute(){

    }
}
