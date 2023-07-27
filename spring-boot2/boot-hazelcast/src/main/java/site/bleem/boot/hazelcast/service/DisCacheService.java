package site.bleem.boot.hazelcast.service;

import site.bleem.boot.hazelcast.anno.HazelcastCache;
import site.bleem.boot.hazelcast.conf.CacheConfig;
import org.springframework.stereotype.Service;

@Service
public class DisCacheService {

    @HazelcastCache(CacheConfig.CACHE_MAP)
    public String addCache(String key){
        return key + "hello distribute!";
    }
}
