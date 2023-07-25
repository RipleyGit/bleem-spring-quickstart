package site.bleem.amqp.source;

import lombok.Data;

@Data
public class BaseInfoSource {
    private String exchange;
    private String routingKey;
    private Object msg;
}
