package site.bleem.boot.kafka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.kafka.producer.ProducerService;

import javax.annotation.Resource;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {
    @Resource
    private ProducerService producerService;

    @GetMapping("/hello/{content}")
    public ResponseEntity<String> settingEquipInfoPlayOrder(@PathVariable("content") @Validated String content) {
        producerService.sendMessage("pp_operate_log",content);
        return ResponseEntity.ok(content);
    }
}
