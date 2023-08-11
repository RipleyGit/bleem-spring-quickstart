package site.bleem.boot.aop.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.aop.anno.OperLogAnno;
import site.bleem.boot.aop.enums.OperLogEnum;

@RestController
public class TestController {




    @RequestMapping(value = "/add/{key}",
            method = RequestMethod.GET)
    @OperLogAnno(OperLogEnum.ADD)
    public ResponseEntity<Object> addKey(@PathVariable("key") String key){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return ResponseEntity.ok("hellow world:"+key);
    }

}
