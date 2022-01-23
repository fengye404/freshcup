package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.pojo.AdminInfoOutput;
import sast.freshcup.pojo.AdminOutput;
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

    @PostMapping("create")
    public String createAdmin(@RequestBody Account account) {
        superAdminService.createAdmin(account);
        return "success";
    }

    @PostMapping("random")
    public List<String> generateAdmin(@RequestParam(name = "number") Integer number,
                                       @RequestParam(name = "password") String password) {
        return superAdminService.randomAdmin(number, password);
    }

    @PostMapping("delete")
    public String deleteAdmin(@RequestParam(name = "uid") Long uid) {
        superAdminService.deleteAdmin(uid);
        return "success";
    }

    @PostMapping("judge")
    public String attributeJudge(@RequestBody @Valid ProblemJudger problemJudger) {
        superAdminService.attributeJudgeAdmin(problemJudger);
        return "success";
    }

    @PostMapping("contest")
    public String attributeContest(@RequestBody @Valid AccountContestManager accountContestManager) {
        superAdminService.attributeContestAdmin(accountContestManager);
        return "success";
    }

    @GetMapping("all")
    public AdminOutput getAllAdmin(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return superAdminService.getAllAdmin(pageNum, pageSize);
    }

    @GetMapping("info")
    public AdminInfoOutput getAdminInfo(@RequestParam(value = "uid") Long uid) {
        return superAdminService.getInfoById(uid);
    }

}
