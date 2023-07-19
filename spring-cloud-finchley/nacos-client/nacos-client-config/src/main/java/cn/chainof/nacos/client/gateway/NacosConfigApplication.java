package cn.chainof.nacos.client.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * nacos 动态更新配置，无法更新运行端口配置
 */
@EnableFeignClients
@RestController
@EnableDiscoveryClient
@SpringBootApplication
@RefreshScope
public class NacosConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class, args);
    }

    @Autowired
    private RemoteClient remoteClient;

    @GetMapping("/feign")
    public String test() {
        System.out.println("进入fegin");
        return remoteClient.helloNacos();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello NACOS";
    }

    @Value("${share.config1}")
    private String shareConfig1;

    @Value("${share.config2}")
    private String shareConfig2;

    @RequestMapping("/getShare1")
    public String getShare1() {
        return shareConfig1;
    }

    @RequestMapping("/getShare2")
    public String getShare2() {
        return shareConfig2;
    }

}