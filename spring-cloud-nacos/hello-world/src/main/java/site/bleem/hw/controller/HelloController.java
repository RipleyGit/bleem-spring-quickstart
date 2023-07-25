package site.bleem.hw.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<String> returnWorld() {
        return ResponseEntity.ok("world!");
    }
}
