package site.bleem.spring.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("this is test", HttpStatus.OK);
    }

}
