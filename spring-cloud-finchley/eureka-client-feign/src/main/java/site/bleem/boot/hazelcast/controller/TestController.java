package site.bleem.boot.hazelcast.controller;

import site.bleem.boot.hazelcast.feign.ClientSentryFeign;
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

    @GetMapping("/timeOut")
    public String whoAmI(@RequestParam int mills) {
        String timeOut = clientSentryFeign.timeOut(mills);
        System.out.println(timeOut);
        return timeOut;
    }
}
