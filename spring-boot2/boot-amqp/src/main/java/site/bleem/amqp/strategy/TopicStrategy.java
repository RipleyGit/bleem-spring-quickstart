package site.bleem.amqp.strategy;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
public interface TopicStrategy {
    void handleMessage(String message);
}
