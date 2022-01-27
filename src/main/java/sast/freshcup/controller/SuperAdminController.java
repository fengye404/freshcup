package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.pojo.AdminInfoVO;
import sast.freshcup.pojo.AdminVO;
import sast.freshcup.service.SuperAdminService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:23
 **/
@RestController
@RequestMapping("superadmin/admin")
@AuthHandle(AuthEnum.SUPER_ADMIN)
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @OperateLog(operDesc = "手动创建管理员")
    @PostMapping("create")
    public String createAdmin(@RequestBody Account account) {
        superAdminService.createAdmin(account);
        return "success";
    }

    @OperateLog(operDesc = "批量创建管理员，密码由超管统一决定")
    @PostMapping("random")
    public List<String> generateAdmin(@RequestParam(name = "number") Integer number,
                                      @RequestParam(name = "password") String password) {
        return superAdminService.randomAdmin(number, password);
    }

    @OperateLog(operDesc = "删除对应管理员")
    @PostMapping("delete")
    public String deleteAdmin(@RequestParam(name = "uid") Long uid) {
        superAdminService.deleteAdmin(uid);
        return "success";
    }

    @OperateLog(operDesc = "分配批卷人")
    @PostMapping("judge")
    public String attributeJudge(@RequestBody @Valid ProblemJudger problemJudger) {
        superAdminService.attributeJudgeAdmin(problemJudger);
        return "success";
    }

    @OperateLog(operDesc = "给管理员分配比赛")
    @PostMapping("contest")
    public String attributeContest(@RequestBody @Valid AccountContestManager accountContestManager) {
        superAdminService.attributeContestAdmin(accountContestManager);
        return "success";
    }

    @OperateLog(operDesc = "查询所有管理员")
    @GetMapping("all")
    public AdminVO getAllAdmin(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return superAdminService.getAllAdmin(pageNum, pageSize);
    }

    @OperateLog(operDesc = "查询管理员具体信息")
    @GetMapping("info")
    public AdminInfoVO getAdminInfo(@RequestParam(value = "uid") Long uid) {
        return superAdminService.getInfoById(uid);
    }

}
