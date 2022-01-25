package sast.freshcup.service;

import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.pojo.AdminOutput;
import sast.freshcup.pojo.UserOutput;

import java.io.IOException;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 22:05
 **/
public interface SuperUserService {

    Map<String, Object> importUserAccount(MultipartFile multipartFile) throws IOException;

    UserOutput getAllContestUser(Long contestId, Integer pageNum, Integer pageSize);

    void attributeContest(AccountContestManager accountContestManager);

    void deleteUserById(Long uid);

    void createUser(String username);

    AdminOutput getAllUsers(Integer pageNum, Integer pageSize);
}
