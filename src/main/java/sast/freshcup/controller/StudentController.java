package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.pojo.ProblemVO;
import sast.freshcup.service.StudentService;

import java.util.Map;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:22
 **/
//@AuthHandle(AuthEnum.STUDENT)
@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/contestList")
    public Map<String, Object> getContestList(Integer pageNum, Integer pageSize) {
        return studentService.getContestList(pageNum, pageSize);
    }

    @GetMapping("/problemList")
    public Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize) {
        return studentService.getProblemList(contestId, pageNum, pageSize);
    }

    @GetMapping("/problem")
    public ProblemVO getProblemById(Long problemId) {
        return studentService.getProblemById(problemId);
    }

    @PostMapping("/answer")
    public void uploadAnswer(Long problemId, Long contestId, String content) {
        studentService.uploadAnswer(contestId, problemId, content);
    }

    @GetMapping("/remainingTime")
    public Map<String, Object> getRemainingTime(Long contestId) {
        return studentService.getRemainingTime(contestId);
    }

}
