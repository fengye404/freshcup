package sast.freshcup.service;

import sast.freshcup.entity.Account;
import sast.freshcup.entity.ProblemJudger;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:03
 **/
public interface SuperAdminService {

    void createAdmin(Account account);

    List<Account> randomAdmin(Integer number);

    void deleteAdmin(Integer uid);

    void attributeContestAdmin(ProblemJudger problemJudger);
}
