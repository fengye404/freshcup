package sast.freshcup.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.entity.ProblemJudger;

import java.io.IOException;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:03
 **/
public interface SuperAdminService {

    Workbook importAdmin(Long contestId, MultipartFile file) throws IOException;

    void deleteAdmin(Long uid);

    void attributeJudgeAdmin(ProblemJudger problemJudger);

    Map<String, Object> getAllAdmin(Integer pageNum, Integer pageSize);

    Map<String, Object> getAllAdmin(Long contestId, Integer pageNum, Integer pageSize);

}
