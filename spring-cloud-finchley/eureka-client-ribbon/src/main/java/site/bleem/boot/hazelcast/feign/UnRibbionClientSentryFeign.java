package site.bleem.boot.hazelcast.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sentry",url = "http://localhost:8010/")
public interface UnRibbionClientSentryFeign {
    @RequestMapping("/timeOut")
    String timeOut(@RequestParam int mills);
}
