package site.bleem.boot.hazelcast.conf;

import site.bleem.boot.hazelcast.anno.ExcludeFromComponentScan;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ExcludeFromComponentScan
public class RibbonLoadConf {
    @Bean
    public IRule ribbonRule(){

//        return new RandomRule();
        return null;
    }
}
