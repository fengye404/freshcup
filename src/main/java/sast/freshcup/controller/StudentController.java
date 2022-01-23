package sast.freshcup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:22
 **/
@RequestMapping("/student")
@RestController
public class StudentController {
    @AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/contestList")
    public void getContestList() {

    }
}
