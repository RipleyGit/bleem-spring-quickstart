package site.bleem.amqp.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(
    prefix = "rabbitmq"
)
public class RabbitMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    private RabbitProperties bleemRibbit;

    @Bean
    @ConditionalOnProperty(
        name = {"rabbitmq.bleemRibbit.enable"}
    )
    public Queue bleemRibbitQueue() {
        Queue queue = new Queue(this.bleemRibbit.getQueueKey(), false, false, false);
        log.info("----{}队列创建成功----", queue.getName());
        return queue;
    }

    @Bean
    @ConditionalOnProperty(
        name = {"rabbitmq.bleemRibbit.enable"}
    )
    public DirectExchange bleemRibbitExchange() {
        DirectExchange directExchange = new DirectExchange(this.bleemRibbit.getExchange(), true, false);
        log.info("----{}交换机创建成功----", directExchange.getName());
        return directExchange;
    }

    @Bean
    @ConditionalOnProperty(
        name = {"rabbitmq.bleemRibbit.enable"}
    )
    public Binding bleemRibbitBinding() {
        return BindingBuilder.bind(this.bleemRibbitQueue()).to(this.bleemRibbitExchange()).with(this.bleemRibbit.getRoutingKey());
    }



}
