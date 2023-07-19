package site.bleem.boot.hazelcast.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Watter on 2018-03-22
 */
@RestController
public class TestController {

    private static String name ="client-sentry-two";

    /**
     * 测试重试时间
     *
     * @return
     */
    @RequestMapping("/timeOut")
    public String timeOut(@RequestParam int mills) {
        System.out.println(String.format("%s收到请求,阻塞%s-ms",name,mills));
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String format = String.format("延时%sms,%s返回请求",mills, name);
        System.out.println(format);
        return format;
    }
}
