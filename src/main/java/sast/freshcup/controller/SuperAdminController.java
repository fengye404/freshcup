package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.service.SuperAdminService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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


    @OperateLog(operDesc = "批量创建管理员，密码由超管统一决定")
    @PostMapping("random")
    public List<String> generateAdmin(@RequestParam(name = "number") Integer number,
                                      @RequestParam(name = "contestId") Long contestId) {
        return null;
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

    @OperateLog(operDesc = "查询所有（对应比赛）管理员")
    @GetMapping("all")
    public Map<String, Object> getAllAdmin(@RequestParam(value = "contestId") Long contestId,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        if (contestId == null) {
            return superAdminService.getAllAdmin(pageNum, pageSize);
        } else {
            return superAdminService.getAllAdmin(contestId, pageNum, pageSize);
        }
    }

}
