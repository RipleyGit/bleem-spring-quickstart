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
public class ServerInfoMessage extends RealtimeInfoMessage{

    private List<ServerInfo> serverInfo;

    @Override
    public void execute(){
        System.out.println("处理服务器状态消息");
    }
}
@Data
class ServerInfo{
    private String type;
    private String ip;
    private Integer port;
    private Integer online;

}