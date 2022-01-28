package sast.freshcup.service;

import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;

import java.util.List;
import java.util.Map;

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

    Map<String, Object> getAllAdmin(Integer pageNum, Integer pageSize);

    Map<String, Object> getAllAdmin(Long contestId, Integer pageNum, Integer pageSize);

}
