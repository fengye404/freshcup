package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.pojo.ProblemVO;
import sast.freshcup.service.StudentService;

import java.time.Duration;
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

    //@AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/contestList")
    public Map<String, Object> getContestList(Integer pageNum, Integer pageSize) {
        return studentService.getContestList(pageNum, pageSize);
    }

    //@AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/problemList")
    public Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize) {
        return studentService.getProblemList(contestId, pageNum, pageSize);
    }

    //@AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/problem")
    public ProblemVO getProblemById(Long problemId) {
        return studentService.getProblemById(problemId);
    }

    //@AuthHandle(AuthEnum.STUDENT)
    @GetMapping("/remainingTime")
    public Map<String,Object> getRemainingTime(Long contestId) {
        return studentService.getRemainingTime(contestId);
    }
}
