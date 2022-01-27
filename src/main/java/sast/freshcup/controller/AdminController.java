package sast.freshcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
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
@AuthHandle(AuthEnum.ADMIN)
public class AdminController {

    @Autowired
    AdminService adminService;

    @OperateLog(operDesc = "管理端获取题目详细信息")
    @GetMapping("/problemInfo")
    Problem getProblemInfo(@RequestParam("id") Long id) {
        return adminService.getProblemInfo(id);
    }

    @OperateLog(operDesc = "管理端删除题目")
    @PostMapping("/deleteProblem")
    String deleteProblem(@RequestParam("id") Long id) {
        return adminService.deleteProblem(id);
    }

    @OperateLog(operDesc = "管理端获取题目")
    @GetMapping("/problems")
    Map<String, Object> getProblemList(Long contestId,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return adminService.getProblemList(contestId, pageNum, pageSize);
    }

    @OperateLog(operDesc = "管理端获取比赛列表")
    @GetMapping("/contests")
    Map<String, Object> getContestList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return adminService.getContestList(pageNum, pageSize);
    }

    @OperateLog(operDesc = "管理端审批题目")
    @PostMapping("/judge")
    String judgeProblem(@RequestBody Judge judge) {
        return adminService.judgeProblem(judge);
    }

    @OperateLog(operDesc = "管理端设置比赛题目")
    @PostMapping("/setProblem")
    String setProblem(@RequestBody Problem problem) {
        return adminService.setProblem(problem);
    }

    @OperateLog(operDesc = "管理端更改题目内容")
    @PostMapping("/updateProblem")
    String updateProblem(@RequestBody Problem problem) {
        return adminService.updateProblem(problem);
    }
}
