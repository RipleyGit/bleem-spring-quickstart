package site.bleem.boot.hazelcast.controller;

import site.bleem.boot.hazelcast.config.HystrixContext;
import site.bleem.boot.hazelcast.feign.ClientSentryFeign;
import site.bleem.boot.hazelcast.feign.ClientSentryFeignPoxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Watter on 2018-03-22
 */
@RestController
public class TestController {

    @Autowired
    private ClientSentryFeign clientSentryFeign;

    @Autowired
    private ClientSentryFeignPoxy clientSentryFeignPoxy;

    @GetMapping("/timeOut")
    public String whoAmI(@RequestParam int mills) {
        String timeOut = clientSentryFeign.timeOut(mills);
        System.out.println(timeOut);
        return timeOut;
    }


    @GetMapping("/hello")
    public String hello(@RequestParam String mills) {
        HystrixContext.getInstance();
        String timeOut1 = clientSentryFeignPoxy.testCache(mills);
        System.out.println("第一次请求："+timeOut1);
        String timeOut2 = clientSentryFeignPoxy.testCache(mills);
        System.out.println("第二次请求："+timeOut2);
        return timeOut2;
    }
}
