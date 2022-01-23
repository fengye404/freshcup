package sast.freshcup.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.entity.ProblemJudger;
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
    public List<Account> randomAdmin(Integer number) {
        return superAdminService.randomAdmin(number);
    }

    @PostMapping("delete")
    public String deleteAdmin(Integer uid) {
        superAdminService.deleteAdmin(uid);
        return "success";
    }

    @PostMapping("judge")
    public String attributeJudge(@RequestBody @Valid ProblemJudger problemJudger) {
        superAdminService.attributeContestAdmin(problemJudger);
        return "success";
    }

    @PostMapping("random")
    public List<Account> generateAdmin(Integer number) {
        return superAdminService.randomAdmin(number);
    }

}
