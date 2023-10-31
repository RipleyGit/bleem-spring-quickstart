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
public class DiskInfoMessage extends RealtimeInfoMessage{
    private Integer total;
    private Integer free;
    private Integer alarm;
    @Override
    public void execute(){
        System.out.println("处理磁盘状态消息");
    }
}
