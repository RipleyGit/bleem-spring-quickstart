package cn.chainof.micro.config.bus;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ConfigRefreshController {

    @Autowired
    private ContextRefresher contextRefresher;

    @GetMapping("/config/refresh")
    public String refresh() {
        Set<String> refresh = contextRefresher.refresh();
        String config = StringUtils.join(refresh);
        System.out.println("刷新配置："+ config);
        return config;
    }
}
