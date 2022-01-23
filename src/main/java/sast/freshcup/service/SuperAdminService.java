package sast.freshcup.service;

import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.pojo.AdminInfoOutput;
import sast.freshcup.pojo.AdminOutput;

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

    AdminOutput getAllAdmin(Integer pageNum, Integer pageSize);

    AdminInfoOutput getInfoById(Long uid);

}
