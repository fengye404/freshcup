package sast.freshcup.service;

import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.pojo.AdminInfoVO;
import sast.freshcup.pojo.AdminVO;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:03
 **/
public interface SuperAdminService {

    void createAdmin(Account account);

    List<String> randomAdmin(Integer number, String password);

    void deleteAdmin(Long uid);

    void attributeJudgeAdmin(ProblemJudger problemJudger);

    void attributeContestAdmin(AccountContestManager accountContestManager);

    AdminVO getAllAdmin(Integer pageNum, Integer pageSize);

    AdminInfoVO getInfoById(Long uid);

}
