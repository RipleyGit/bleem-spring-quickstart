package site.bleem.boot.thread.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.thread.thread.BleemThreadManager;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {
    @GetMapping("/world/{world}")
    public ResponseEntity<String> returnWorld(@PathVariable("world") String world) {
        BleemThreadManager.getInstance().enqueue(world, 100);
        BleemThreadManager.getInstance().activation();
        return ResponseEntity.ok("world!");
    }
}
