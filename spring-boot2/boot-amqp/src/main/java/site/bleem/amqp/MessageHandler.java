package site.bleem.amqp;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
public interface MessageHandler {
    /**
     * 消息处理接口
     *
     * @param topic
     * @param message
     */
    void handle(String topic, String message);
}