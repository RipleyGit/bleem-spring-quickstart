package site.bleem.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.amqp.util.RabbitmqUtil;

import javax.annotation.Resource;

@RestController
@SpringBootApplication
public class AmqpMain {
    public static void main(String[] args) {
        SpringApplication.run(AmqpMain.class, args);
    }

    @Value("${rabbitmq.bleemRibbit.routingKey}")
    private String routingKey;
    @Resource
    private RabbitmqUtil rabbitmqUtil;

    @PutMapping("/hello/{id}")
    public ResponseEntity helloPut(@PathVariable String id){
        rabbitmqUtil.sendMsqToQueue(routingKey,id);
        return ResponseEntity.ok().build();
    }

    @RabbitListener(queues = "bleem_ribbit_route")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // 在这里处理消息的逻辑
    }

}