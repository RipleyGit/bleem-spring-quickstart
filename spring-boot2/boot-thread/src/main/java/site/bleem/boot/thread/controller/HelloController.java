package site.bleem.boot.thread.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.thread.thread.BleemThreadTaskPoolManager;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {
    @GetMapping("/task/{user}/{task}/{count}")
    public ResponseEntity<String> returnWorld(@PathVariable("user") String user,@PathVariable("task") String task,@PathVariable("count") Integer count) {
        BleemThreadTaskPoolManager.getInstance().enqueue(task,user,count);
        BleemThreadTaskPoolManager.getInstance().activation();//激活线程运行
        return ResponseEntity.ok("world!");
    }
}
