package site.bleem.boot.hazelcast.conf;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheConfig {

    public final static String HAZELCAST_INSTANCE = "hazelcast-instance-test";

    public final static String CACHE_MAP = "testCache";

    @Bean
    public Config hazelCastConfig() {
        //如果有集群管理中心，可以配置
        ManagementCenterConfig centerConfig = new ManagementCenterConfig();
//        centerConfig.setUrl("http://"+ IPV4Util.getIpAddress() +":8200/mancenter");
        centerConfig.setEnabled(true);
        return new Config()
                .setInstanceName(HAZELCAST_INSTANCE)
                .setManagementCenterConfig(centerConfig)
                .addMapConfig(
                        new MapConfig()
                                .setName("testCache")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(20000));
    }
}
