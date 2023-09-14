package site.bleem.boot.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.event.evevnt.UserChangePasswordEvent;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationEventPublisher appEventPublisher;

    @GetMapping("/hello/{world}")
    public ResponseEntity<String> returnWorld(@PathVariable("world") String world) {

        applicationContext.publishEvent(new UserChangePasswordEvent(world));

        appEventPublisher.publishEvent(new UserChangePasswordEvent(world));

        return ResponseEntity.ok(world);
    }
}
