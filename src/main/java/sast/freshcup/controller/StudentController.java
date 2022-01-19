package sast.freshcup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.exception.LocalRunTimeException;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:22
 **/
@RestController
public class StudentController {
    @AuthHandle(AuthEnum.ADMIN)
    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    @AuthHandle(AuthEnum.SUPER_ADMIN)
    @GetMapping("/test1")
    public String test1(){
        return "OK";
    }
}
