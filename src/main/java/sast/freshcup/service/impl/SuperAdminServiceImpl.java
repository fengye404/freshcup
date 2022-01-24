package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.entity.Contest;
import sast.freshcup.entity.Problem;
import sast.freshcup.entity.ProblemJudger;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.AccountMapper;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.mapper.ProblemJudgerMapper;
import sast.freshcup.mapper.ProblemMapper;
import sast.freshcup.service.SuperAdminService;
import sast.freshcup.util.RandomUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:10
 **/
@Service
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {

    private final AccountMapper accountMapper;

    private final ProblemJudgerMapper problemJudgerMapper;

    private final ContestMapper contestMapper;

    private final ProblemMapper problemMapper;

    private final RandomUtil randomUtil;

    public SuperAdminServiceImpl(AccountMapper accountMapper, ProblemJudgerMapper problemJudgerMapper, RandomUtil randomUtil, ContestMapper contestMapper, ProblemMapper problemMapper) {
        this.accountMapper = accountMapper;
        this.problemJudgerMapper = problemJudgerMapper;
        this.randomUtil = randomUtil;
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
    }

    @Override
    public void createAdmin(Account account) {
        account.setPassword(DigestUtils.md5DigestAsHex(account.getPassword().getBytes()));
        account.setRole(1);
        accountMapper.insert(account);
    }

    @Override
    public List<Account> randomAdmin(Integer number) {
        List<Account> accounts = new LinkedList<>();
        for (int i = 0; i < number; i++) {
            Account account = new Account(randomUtil.getStr(),
                    randomUtil.getStr(),
                    1);
            accounts.add(new Account(account.getUsername(), account.getPassword(), 1));
            account.setPassword(DigestUtils.md5DigestAsHex(account.getPassword().getBytes()));
            accountMapper.insert(account);
        }
        return accounts;
    }

    @Override
    public void deleteAdmin(Integer uid) {
        accountMapper.deleteById(uid);
    }

    @Override
    public void attributeContestAdmin(ProblemJudger problemJudger) {
        QueryWrapper<Contest> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("cid", problemJudger.getContestId());
        if (contestMapper.selectOne(queryWrapper1) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        QueryWrapper<Problem> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("id", problemJudger.getProblemId());
        if (problemMapper.selectOne(queryWrapper2) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_PROBLEM);
        }
        QueryWrapper<Account> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("uid", problemJudger.getJudgerId());
        if (accountMapper.selectOne(queryWrapper3) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        problemJudgerMapper.insert(problemJudger);
    }

}
