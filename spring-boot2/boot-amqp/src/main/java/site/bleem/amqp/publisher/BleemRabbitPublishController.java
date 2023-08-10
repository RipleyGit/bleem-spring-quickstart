package site.bleem.amqp.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.amqp.util.RabbitmqUtil;

import javax.annotation.Resource;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/10
 */
@RestController
public class BleemRabbitPublishController {

    @Value("${rabbitmq.bleemRibbit.routingKey}")
    private String routingKey;
    @Value("${rabbitmq.bleemRibbit.exchange}")
    private String exchange;
    @Value("${rabbitmq.bleemRibbit.queueKey}")
    private String queueKey;
    @Resource
    private RabbitmqUtil rabbitmqUtil;

    @GetMapping("/hello/{id}")
    public ResponseEntity helloPut(@PathVariable String id){
        rabbitmqUtil.sendMsqToQueue(queueKey,id);
        return ResponseEntity.ok().build();
    }
}
