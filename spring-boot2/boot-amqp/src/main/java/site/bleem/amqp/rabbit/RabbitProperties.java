package site.bleem.amqp.rabbit;

import lombok.Data;

@Data
public class RabbitProperties {
    private String exchange;
    private String routingKey;
    private String queueKey;
}
