package site.bleem.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/10
 */
public class BleemRabbitConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "${rabbitmq.bleemRibbit.queueKey}",
                            durable = "false",
                            autoDelete = "true"
                    ),
                    exchange = @Exchange(value = "${rabbitmq.bleemRibbit.exchange}"),
                    key = "${rabbitmq.bleemRibbit.routingKey}"))
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // 在这里处理消息的逻辑
    }

}
