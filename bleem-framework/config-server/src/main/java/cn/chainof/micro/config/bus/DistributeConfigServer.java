package cn.chainof.micro.config.bus;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
@ConditionalOnClass(EnableConfigServer.class)
public class DistributeConfigServer implements ApplicationRunner {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaInstanceConfig eurekaInstanceConfig;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        String configServerName = eurekaInstanceConfig.getAppname();

        List<String> services = discoveryClient.getServices();
        for (String service:services){
            if (configServerName.equals(service)){
                continue;
            }
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances){
                RestTemplate restTemplate = new RestTemplate();
                URI uri = URI.create(String.format("http://%s:%s/%s", instance.getHost(), instance.getPort(), "config/refresh"));
                restTemplate.getForObject(uri,String.class);
            }
        }
    }
}
