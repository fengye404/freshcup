package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.pojo.ContestListVO;
import sast.freshcup.pojo.ProblemListVO;
import sast.freshcup.service.SuperContestService;

import javax.validation.Valid;

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
    public ContestListVO getAllContest(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllContest(pageNum, pageSize);
    }

    @OperateLog(operDesc = "题目排序")
    @PostMapping("sort")
    public String problemSort(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "orderId") Integer orderId) {
        superContestService.problemSort(id, orderId);
        return "success";
    }

    @OperateLog(operDesc = "获取题目信息")
    @GetMapping("problem")
    public ProblemListVO getAllProblem(@RequestParam(name = "contestId") Long contestId,
                                       @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllProblem(contestId, pageNum, pageSize);
    }

    @OperateLog(operDesc = "手动同步 redis 缓存")
    @PostMapping("answer")
    public String uploadAnswer() {
        superContestService.answerUpload();
        return "success";
    }

}
