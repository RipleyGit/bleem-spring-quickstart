package cn.chainof.micro.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientTestController {

    @Value("${world}")
    private String value;

    @Autowired
    private ContextRefresher contextRefresher;

    @RequestMapping("/world")
    public String hello() {
        contextRefresher.refresh();
        return "world" + value;
    }
}
