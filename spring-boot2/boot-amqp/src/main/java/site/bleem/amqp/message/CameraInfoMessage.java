package site.bleem.amqp.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@Slf4j
@Data
public class CameraInfoMessage extends RealtimeInfoMessage{
    private List<CameraState> cameraStates;
    @Override
    public void execute(){
        System.out.println("处理摄像仪状态消息");
    }
}
@Data
class CameraState{
    private Integer cameraId;
    private Integer cameraState;
}