package cn.chainof.nacos.client.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nacos-provide")
public interface RemoteClient {

    @GetMapping("/helloNacos")
    String helloNacos();
}