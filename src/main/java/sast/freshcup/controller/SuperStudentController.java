package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
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
    public String createUser(@RequestParam(value = "username") String username,
                             @RequestParam(value = "contestId") Long contestId) {

        return "success";
    }

    @OperateLog(operDesc = "导入学生")
    @PostMapping("import")
    public Map<String, Object> getUsersByContestId(MultipartFile user_data) throws IOException {
        return superUserService.importUserAccount(user_data);
    }

    @OperateLog(operDesc = "查询所有学生信息")
    @GetMapping("all")
    public Map<String, Object> getAllUsers(@RequestParam(name = "contestId") Long contestId,
                                           @RequestParam(name = "pageNum", defaultValue = "1")
                                                   Integer pageNum,
                                           @RequestParam(name = "pageSize", defaultValue = "5")
                                                   Integer pageSize) {
        if (contestId == null) {
            return superUserService.getAllContestUser(pageNum, pageSize);
        } else {
            return superUserService.getAllContestUser(contestId, pageNum, pageSize);
        }
    }

}
