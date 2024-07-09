package site.bleem;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class AmqpApplication {
  @Autowired
  private AmqpTemplate rabbitTemplate;
  @Autowired
  private AmqpAdmin amqpAdmin;

  public static void main(String[] args) {
    SpringApplication.run(AmqpApplication.class, args);
  }

  private static ConcurrentHashSet<String> ramqQueueSet =new ConcurrentHashSet<>();

  private void createRamqQueue(String topicName){
    if (ramqQueueSet.contains(topicName)){
      log.info("队列[{}]已存在");
      return;
    }
    DirectExchange directExchange = new DirectExchange(topicName+"_ex", true, false);
    log.info("----{}交换机创建成功----", directExchange.getName());
    Queue queue = new Queue(topicName, false, false, false);
    log.info("----{}队列创建成功----", queue.getName());
    Binding topicNameBinding = BindingBuilder.bind(queue).to(directExchange).with(topicName + "_rk");
    ramqQueueSet.add(topicName);
  }
  @GetMapping("/send/{message}")
  public String sendMessage(@PathVariable String message) {
    // 检查 Exchange 是否存在，如果不存在则创建
    String sxbychem2BestwayCompanyTopic = "sxbychem_2_bestway_company";
    createRamqQueue(sxbychem2BestwayCompanyTopic);
    Exchange exchange = new DirectExchange(sxbychem2BestwayCompanyTopic);
    amqpAdmin.declareExchange(exchange);
    rabbitTemplate.convertAndSend(sxbychem2BestwayCompanyTopic,sxbychem2BestwayCompanyTopic,message);
//    rabbitTemplate.sendMessage("myQueue", message);
    return "Message sent successfully!";
  }

  private boolean exchangeExists(String exchangeName) {
    try {
      amqpAdmin.getQueueInfo(exchangeName);
//      amqpAdmin.
//      amqpAdmin.getExchangeInfo(exchangeName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
