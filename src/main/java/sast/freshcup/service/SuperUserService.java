package sast.freshcup.service;

import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.pojo.UserOutput;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 22:05
 **/
public interface SuperUserService {

    void importUserAccount(MultipartFile multipartFile);

    UserOutput getAllContestUser(Long contestId, Integer pageNum, Integer pageSize);

    void attributeContest(AccountContestManager accountContestManager);

    void deleteUserById(Long uid);

    void createUser(String username);
}
