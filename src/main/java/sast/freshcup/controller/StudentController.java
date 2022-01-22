package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.pojo.ContestVO;
import sast.freshcup.service.StudentService;

import java.util.List;
import java.util.Map;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:22
 **/
@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/contestList")
    public Map<String,Object> getContestList(Integer pageNum, Integer pageSize) {
        List<ContestVO> contestList = studentService.getContestList(pageNum, pageSize);
        return null;
    }
}
