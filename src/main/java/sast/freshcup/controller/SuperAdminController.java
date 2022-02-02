package sast.freshcup.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.service.SuperAdminService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    //    @OperateLog(operDesc = "查询所有（对应比赛）管理员")
    @GetMapping("all")
    public Map<String, Object> getAllAdmin(@RequestParam(value = "contestId", required = false)
                                                   Long contestId,
                                           @RequestParam(value = "pageNum", defaultValue = "1")
                                                   Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "5")
                                                   Integer pageSize) {
        if (contestId == null) {
            return superAdminService.getAllAdmin(pageNum, pageSize);
        } else {
            return superAdminService.getAllAdmin(contestId, pageNum, pageSize);
        }
    }

    @OperateLog(operDesc = "导入管理员账号，同时导出结果")
    @PostMapping(value = "import", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void importAdmin(@RequestParam(name = "contestId") Long contestId,
                            @RequestBody MultipartFile admin_data,
                            HttpServletResponse response) {
        String fileName = "管理员名单_" + LocalDateTime.now() + ".xlsx";
        Workbook wb = null;
        try {
            wb = superAdminService.importAdmin(contestId, admin_data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputExcel(response, fileName, wb);
    }

    static void outputExcel(HttpServletResponse response, String fileName, Workbook wb) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        if (wb == null) {
            throw new LocalRunTimeException(ErrorEnum.EXPORT_ERROR);
        }

        try {
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
