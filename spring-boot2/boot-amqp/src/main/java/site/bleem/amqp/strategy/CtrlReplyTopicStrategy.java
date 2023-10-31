package site.bleem.amqp.strategy;

import org.springframework.stereotype.Component;

/**
 * @author yubs
 * @desc todo
 * @date 2023/10/31
 */
@Component("ctrlCommand")
public class CtrlReplyTopicStrategy implements TopicStrategy {

    @Override
    public void handleMessage(String message){

    }

}