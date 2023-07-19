package site.bleem.boot.hazelcast.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sentry")
public interface ClientSentryFeign {
    @RequestMapping("/timeOut")
    String timeOut(@RequestParam int mills);

    @RequestMapping("/testCache")
    String testCache(@RequestParam String str);
}
