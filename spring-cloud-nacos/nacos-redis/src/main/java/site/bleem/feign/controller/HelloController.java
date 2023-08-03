package site.bleem.feign.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.feign.service.RedisService;

import javax.annotation.Resource;
import java.util.HashMap;


/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {

    @Resource
    private RedisService redisService;



    @GetMapping("/hello")
    public ResponseEntity<String> returnWorld() {
        return ResponseEntity.ok("world!");
    }

    @GetMapping("/hello/{content}/{time}")
    public ResponseEntity<String> settingEquipInfoPlayOrder(@PathVariable("content") @Validated String content,@PathVariable("time") @Validated Integer time) throws Exception {
        redisService.set("hello",content,Long.valueOf(10));
        return ResponseEntity.ok(content);
    }
}