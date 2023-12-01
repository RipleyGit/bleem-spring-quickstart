package site.bleem.boot.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.web.common.AccessLimit;
import site.bleem.boot.web.validate.IdCard;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
public class WebController {
    @AccessLimit(message="参数已处理过")
    @GetMapping("/hello/{word}")
    public ResponseEntity<String> returnWorld(@PathVariable("word") String word) {
        return ResponseEntity.ok("hello "+word);
    }

    @GetMapping("/id")
    public ResponseEntity<String> idCode(@RequestParam("idCode") @Validated @NotBlank String idCode) {
        return ResponseEntity.ok("hello "+idCode);
    }
}