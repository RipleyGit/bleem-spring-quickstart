package cn.chainof.nacos.client.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
public class NacosGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NacosGatewayApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
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

}