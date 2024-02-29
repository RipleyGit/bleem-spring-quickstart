package site.bleem.boot.hazelcast;

import site.bleem.boot.hazelcast.anno.ExcludeFromComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type= FilterType.ANNOTATION,value = ExcludeFromComponentScan.class)})
public class ClientRibbonApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClientRibbonApplication.class, args);
	}
}
