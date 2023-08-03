package site.bleem.feign;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class NacosRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosRedisApplication.class, args);
    }

}