package site.bleem.feign.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.feign.config.RedissonDelayQueue;
import site.bleem.feign.service.RedisService;

import javax.annotation.Resource;


/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class RedisController {

    @Resource
    private RedisService redisService;

    @Resource
    private RedissonDelayQueue redissonDelayQueue;



    @GetMapping("/hello")
    public ResponseEntity<String> returnWorld() {
        return ResponseEntity.ok("world!");
    }

    @GetMapping("/ecpire/{key}/{time}")
    public ResponseEntity<String> ecpire(@PathVariable("key") @Validated String key,@PathVariable("time") @Validated Integer time) throws Exception {
        redisService.set("hello",key, Long.valueOf(time));
        return ResponseEntity.ok(key);
    }
    @GetMapping("/queue/{key}/{time}")
    public ResponseEntity<String> queue(@PathVariable("key") @Validated String key,@PathVariable("time") @Validated Integer time) throws Exception {
        redissonDelayQueue.offerTask(key,time);
        return ResponseEntity.ok(key);
    }
}