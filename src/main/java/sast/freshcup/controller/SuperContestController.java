package sast.freshcup.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.entity.Contest;
import sast.freshcup.service.SuperContestService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:47
 **/
@RestController
@RequestMapping("superadmin/contest")
//@AuthHandle(AuthEnum.SUPER_ADMIN)
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
    public String problemSort(@RequestBody Map<String, Object> json) {
        superContestService.problemSort(
                (List<Long>) json.get("problemIds"),
                Long.parseLong(json.get("contestId").toString())
        );
        return "success";
    }

    @OperateLog(operDesc = "结束比赛，同步redis缓存")
    @PostMapping("finish")
    public String finishProblem() {
        superContestService.answerUpload();
        return "success";
    }

    @OperateLog(operDesc = "重置比赛账号")
    @PostMapping("reset")
    public String destroyContest(Long contestId) {
        superContestService.resetContest(contestId);
        return "success";
    }

    @OperateLog(operDesc = "获取比赛题目")
    @GetMapping("problem")
    public Map<String, Object> getAllProblem(@RequestParam(name = "contestId") Long contestId,
                                             @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllProblem(contestId, pageNum, pageSize);
    }

    //    @OperateLog(operDesc = "导出比赛结果")
    @PostMapping("result")
    public void exportResult(@RequestParam(name = "contestId") Long contestId,
                             HttpServletResponse response) {

        String fileName = "成绩名单_" + LocalDateTime.now() + ".xlsx";
        Workbook wb = null;
        try {
            wb = superContestService.exportResult(contestId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SuperAdminController.outputExcel(response, fileName, wb);
    }

}
