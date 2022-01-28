package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.service.SuperContestService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:47
 **/
@RestController
@RequestMapping("superadmin/contest")
@AuthHandle(AuthEnum.ADMIN)
public class SuperContestController {

    private final SuperContestService superContestService;

    public SuperContestController(SuperContestService superContestService) {
        this.superContestService = superContestService;
    }

    @OperateLog(operDesc = "创建比赛")
    @PostMapping("create")
    public String createContest(@RequestBody @Valid Contest contest) {
        superContestService.createContest(contest);
        return "success";
    }

    @OperateLog(operDesc = "编辑比赛")
    @PostMapping("edit")
    public String editContest(@RequestBody @Valid Contest contest) {
        superContestService.editContest(contest);
        return "success";
    }

    @OperateLog(operDesc = "删除比赛")
    @PostMapping("delete")
    public String deleteContest(Long id) {
        superContestService.deleteContest(id);
        return "success";
    }

    @OperateLog(operDesc = "获取比赛具体信息")
    @GetMapping("info")
    public Contest getContest(Long id) {
        return superContestService.getContestById(id);
    }

    @OperateLog(operDesc = "获取所有比赛信息")
    @GetMapping("all")
    public Map<String, Object> getAllContest(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllContest(pageNum, pageSize);
    }

    @OperateLog(operDesc = "题目排序")
    @PostMapping("sort")
    public String problemSort(@RequestBody HashMap<String, Object> request) {

        return "success";
    }

    @OperateLog(operDesc = "结束比赛，同步redis缓存")
    @PostMapping("sort")
    public String finishProblem() {
        superContestService.answerUpload();
        return "success";
    }

    @OperateLog(operDesc = "删除比赛所有学生、管理员账号")
    @PostMapping("destroy")
    public String destroyContest(Long contestId) {

        return "success";
    }

    @OperateLog(operDesc = "获取比赛题目")
    @GetMapping("problem")
    public Map<String, Object> getAllProblem(@RequestParam(name = "contestId") Long contestId,
                                             @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllProblem(contestId, pageNum, pageSize);
    }

    @OperateLog(operDesc = "导出比赛结果")
    @PostMapping("result")
    public String exportResult(@RequestParam(name = "contestId") Long contestId) {

        return "success";
    }

}
