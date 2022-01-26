package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.service.RedisService;

import java.util.Set;

/**
 * @author: 風楪fy
 * @create: 2022-01-26 16:01
 **/
@RestController
public class TestController {


    @Autowired
    private RedisService redisService;

    @GetMapping("/test")
    public void test(){
        Set<String> keys = redisService.getKeys("ANSWER");
        for (String key : keys) {
            System.out.println(key);
        }
    }
}
