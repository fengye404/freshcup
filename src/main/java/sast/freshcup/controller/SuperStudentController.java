package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.pojo.AdminVO;
import sast.freshcup.pojo.UserVO;
import sast.freshcup.service.SuperUserService;

import java.io.IOException;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 14:49
 **/
@RestController
@RequestMapping("superadmin/user")
@AuthHandle(AuthEnum.SUPER_ADMIN)
public class SuperStudentController {

    private final SuperUserService superUserService;

    public SuperStudentController(SuperUserService superUserService) {
        this.superUserService = superUserService;
    }

    @OperateLog(operDesc = "删除学生账号")
    @PostMapping("delete")
    public String deleteUserById(Long uid) {
        superUserService.deleteUserById(uid);
        return "success";
    }

    @OperateLog(operDesc = "手动创建学生账号")
    @PostMapping("create")
    public String createUser(@RequestParam(value = "username") String username) {
        superUserService.createUser(username);
        return "success";
    }

    @OperateLog(operDesc = "学生分配比赛")
    @PostMapping("contest")
    public String attributeContest(@RequestBody AccountContestManager accountContestManager) {
        superUserService.attributeContest(accountContestManager);
        return "success";
    }

    @OperateLog(operDesc = "获取对应比赛的学生")
    @GetMapping("info")
    public UserVO getUsersByContestId(@RequestParam(name = "contestId") Long contestId,
                                      @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superUserService.getAllContestUser(contestId, pageNum, pageSize);
    }

    @OperateLog(operDesc = "导入学生")
    @PostMapping("import")
    public Map<String, Object> getUsersByContestId(MultipartFile user_data) throws IOException {
        return superUserService.importUserAccount(user_data);
    }

    @OperateLog(operDesc = "查询所有学生信息")
    @GetMapping("all")
    public AdminVO getAllUsers(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superUserService.getAllUsers(pageNum, pageSize);
    }

}
