package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sast.freshcup.entity.Judge;
import sast.freshcup.entity.Problem;
import sast.freshcup.service.AdminService;

import java.util.Map;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:22
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/problemInfo")
    Problem getProblemInfo(@RequestParam("id") Long id) {
        return adminService.getProblemInfo(id);
    }

    @PostMapping("/deleteProblem")
    String deleteProblem(@RequestParam("id") Long id) {
        return adminService.deleteProblem(id);
    }

    @GetMapping("/problems")
    Map<String, Object> getProblemList(Long contestId,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return adminService.getProblemList(contestId, pageNum, pageSize);
    }

    @GetMapping("/contests")
    Map<String, Object> getContestList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return adminService.getContestList(pageNum, pageSize);
    }

    @PostMapping("/judge")
    String judgeProblem(@RequestBody Judge judge) {
        return adminService.judgeProblem(judge);
    }

    @PostMapping("/setProblem")
    String setProblem(@RequestBody Problem problem) {
        return adminService.setProblem(problem);
    }

    @PostMapping("/updateProblem")
    String updateProblem(@RequestBody Problem problem) {
        return adminService.updateProblem(problem);
    }


}
