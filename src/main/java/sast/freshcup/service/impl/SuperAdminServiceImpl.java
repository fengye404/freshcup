package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.*;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.*;
import sast.freshcup.pojo.AdminInfoVO;
import sast.freshcup.pojo.AdminVO;
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

    private final AccountContestManagerMapper accountContestManagerMapper;

    private final RandomUtil randomUtil;

    public SuperAdminServiceImpl(AccountMapper accountMapper,
                                 ProblemJudgerMapper problemJudgerMapper,
                                 RandomUtil randomUtil, ContestMapper contestMapper, ProblemMapper problemMapper,
                                 AccountContestManagerMapper accountContestManagerMapper) {
        this.accountMapper = accountMapper;
        this.problemJudgerMapper = problemJudgerMapper;
        this.randomUtil = randomUtil;
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
        this.accountContestManagerMapper = accountContestManagerMapper;
    }

    /**
     * @param account 超管输入的管理员账号，密码=用户名（学号）
     * @Description: 手动创建管理员账号
     */
    @Override
    public void createAdmin(Account account) {
        account.setPassword(DigestUtils.md5DigestAsHex(account.getPassword().getBytes()));
        account.setRole(1);
        accountMapper.insert(account);
    }

    /**
     * @param number   所需账号数目
     * @param password 统一的密码
     * @return 账号列表
     * @Description: 随机生成账号
     */
    @Override
    public List<String> randomAdmin(Integer number, String password) {
        List<String> accounts = new LinkedList<>();
        for (int i = 0; i < number; i++) {
            Account account = new Account(randomUtil.getStr(),
                    DigestUtils.md5DigestAsHex(password.getBytes()), 1);
            accounts.add(account.getUsername());
            accountMapper.insert(account);
        }
        return accounts;
    }

    /**
     * @param uid 对应 uid
     * @Description: 删除对应管理员账号
     */
    @Override
    public void deleteAdmin(Long uid) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Account account = accountMapper.selectOne(queryWrapper);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        //如果删除对象不为管理员则报错
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        accountMapper.deleteById(uid);
    }

    /**
     * @param problemJudger
     * @Description: 分配批卷人
     */
    @Override
    public void attributeJudgeAdmin(ProblemJudger problemJudger) {
        QueryWrapper<ProblemJudger> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", problemJudger.getJudgerId())
                .eq("contest_id", problemJudger.getContestId())
                .eq("problem_id", problemJudger.getProblemId());
        //防止重复分配
        if (problemJudgerMapper.selectOne(queryWrapper) != null) {
            throw new LocalRunTimeException(ErrorEnum.USER_EXIST);
        }
        QueryWrapper<Contest> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", problemJudger.getContestId());
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
        Account account = accountMapper.selectOne(queryWrapper3);
        //对披卷人身份进行判断
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        problemJudgerMapper.insert(problemJudger);
    }

    /**
     * @param accountContestManager
     * @Description: 给比赛分配批卷人
     */
    @Override
    public void attributeContestAdmin(AccountContestManager accountContestManager) {
        QueryWrapper<AccountContestManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", accountContestManager.getUid())
                .eq("contest_id", accountContestManager.getContestId());
        if (accountContestManagerMapper.selectOne(queryWrapper) != null) {
            throw new LocalRunTimeException(ErrorEnum.USER_EXIST);
        }
        QueryWrapper<Contest> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", accountContestManager.getContestId());
        if (contestMapper.selectOne(queryWrapper1) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        QueryWrapper<Account> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("uid", accountContestManager.getUid());
        Account account = accountMapper.selectOne(queryWrapper2);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        accountContestManagerMapper.insert(accountContestManager);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 获取所有管理员信息
     */
    @Override
    public AdminVO getAllAdmin(Integer pageNum, Integer pageSize) {
        Page<Account> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", 1);
        Page<Account> data = accountMapper.selectPage(page, queryWrapper);
        return new AdminVO(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

    /**
     * @param uid
     * @return
     * @Description: 获取管理员具体信息
     */
    @Override
    public AdminInfoVO getInfoById(Long uid) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Account account = accountMapper.selectOne(queryWrapper);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        QueryWrapper<AccountContestManager> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uid", uid);
        QueryWrapper<ProblemJudger> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("judger_id", uid);
        return new AdminInfoVO(accountContestManagerMapper.selectList(queryWrapper1),
                problemJudgerMapper.selectList(queryWrapper2));
    }

}
