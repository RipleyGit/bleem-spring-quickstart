package site.bleem.boot.hazelcast.controller;

import site.bleem.boot.hazelcast.service.DisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {


    @Autowired
    private DisCacheService disCacheService;

    @RequestMapping(value = "/add/{key}",
            method = RequestMethod.GET)
    public ResponseEntity<Object> addKey(@PathVariable("key") String key){
        String cache = disCacheService.addCache(key);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(cache,headers, HttpStatus.OK);
    }

}
