package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.*;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.*;
import sast.freshcup.mapper.vomapper.AccountVOMapper;
import sast.freshcup.pojo.AccountVO;
import sast.freshcup.service.SuperAdminService;
import sast.freshcup.util.RandomUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final AccountVOMapper accountVOMapper;

    public SuperAdminServiceImpl(AccountMapper accountMapper,
                                 ProblemJudgerMapper problemJudgerMapper,
                                 RandomUtil randomUtil, ContestMapper contestMapper, ProblemMapper problemMapper,
                                 AccountContestManagerMapper accountContestManagerMapper, AccountVOMapper accountVOMapper) {
        this.accountMapper = accountMapper;
        this.problemJudgerMapper = problemJudgerMapper;
        this.randomUtil = randomUtil;
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
        this.accountContestManagerMapper = accountContestManagerMapper;
        this.accountVOMapper = accountVOMapper;
    }

    /**
     * @param contestId
     * @param file
     * @return
     * @throws IOException
     * @Description: 导入管理员
     */
    @Override
    public Workbook importAdmin(Long contestId, MultipartFile file)
            throws IOException {
        if (contestMapper.selectOne(new LambdaQueryWrapper<Contest>()
                .eq(Contest::getId, contestId)) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);

        Workbook output = new XSSFWorkbook();
        Sheet sheetOutput = output.createSheet("结果");
        Row rowOutput = sheetOutput.createRow(0);
        rowOutput.createCell(0).setCellValue("账号");
        rowOutput.createCell(1).setCellValue("密码");

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            String username = cell.getStringCellValue();
            Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>()
                    .eq(Account::getUsername, username));
            if (account != null) {
                if (!account.getRole().equals(1)) {
                    throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
                }
                log.info(username + " 管理员已导入，无需再次导入");
                failure.getAndIncrement();
            } else {
                log.info(username + " 管理员导入成功");
                success.getAndIncrement();

                rowOutput = sheetOutput.createRow(success.get());
                rowOutput.createCell(0).setCellValue(username);
                String password = createAdmin(username);
                rowOutput.createCell(1).setCellValue(password);
            }
            addContestUser(contestId, username, accountMapper, accountContestManagerMapper);
        }
        if (sheetOutput.getLastRowNum() == 0) {
            sheetOutput.createRow(1);
        }
        sheetOutput.getRow(0).createCell(3).setCellValue("成功导入" + success.get() + "个");
        sheetOutput.getRow(1).createCell(3).setCellValue("失败" + failure.get() + "个");
        return output;
    }

    static void addContestUser(Long contestId, String username,
                               AccountMapper accountMapper,
                               AccountContestManagerMapper accountContestManagerMapper) {
        Account account;
        account = accountMapper.selectOne(
                new LambdaQueryWrapper<Account>().eq(Account::getUsername, username)
        );
        if (accountContestManagerMapper.selectOne(
                new LambdaQueryWrapper<AccountContestManager>()
                        .eq(AccountContestManager::getUid, account.getUid())
                        .eq(AccountContestManager::getContestId, contestId)) == null) {
            accountContestManagerMapper.insert(
                    new AccountContestManager(contestId, account.getUid())
            );
        }
    }

    /**
     * @param uid 对应 uid
     * @Description: 删除对应管理员账号
     */
    @Override
    public void deleteAdmin(Long uid) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUid, uid);
        Account account = accountMapper.selectOne(queryWrapper);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        //如果删除对象不为管理员则报错
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        accountMapper.delete(queryWrapper);

        accountContestManagerMapper.delete(
                new LambdaQueryWrapper<AccountContestManager>()
                        .eq(AccountContestManager::getUid, uid));

        problemJudgerMapper.delete(
                new LambdaQueryWrapper<ProblemJudger>()
                        .eq(ProblemJudger::getJudgerId, uid));
    }

    /**
     * @param problemJudger
     * @Description: 分配批卷人
     */
    @Override
    public void attributeJudgeAdmin(ProblemJudger problemJudger) {
        LambdaQueryWrapper<ProblemJudger> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProblemJudger::getJudgerId, problemJudger.getJudgerId())
                .eq(ProblemJudger::getContestId, problemJudger.getContestId())
                .eq(ProblemJudger::getProblemId, problemJudger.getProblemId());
        //防止重复分配
        if (problemJudgerMapper.selectOne(queryWrapper) != null) {
            throw new LocalRunTimeException(ErrorEnum.USER_EXIST);
        }

        if (contestMapper.selectOne(
                new LambdaQueryWrapper<Contest>()
                        .eq(Contest::getId, problemJudger.getContestId())) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }

        if (problemMapper.selectOne(
                new LambdaQueryWrapper<Problem>()
                        .eq(Problem::getId, problemJudger.getProblemId())) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_PROBLEM);
        }

        Account account = accountMapper.selectOne(
                new LambdaQueryWrapper<Account>()
                        .eq(Account::getUid, problemJudger.getJudgerId())
        );
        //对批卷人身份进行判断
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 1) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }

        problemJudgerMapper.insert(problemJudger);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 如果没有输入比赛id就获取所有管理员信息，否则只获取对应比赛管理员
     */
    @Override
    public Map<String, Object> getAllAdmin(Integer pageNum, Integer pageSize) {
        Page<AccountVO> data = accountVOMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<AccountVO>().eq(AccountVO::getRole, 1)
        );
        return getResultMap(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

    @Override
    public Map<String, Object> getAllAdmin(Long contestId, Integer pageNum, Integer pageSize) {
        return getResultMap(accountMapper.getUsersByContestId(contestId, 1,
                        pageNum, pageSize),
                accountMapper.getUsersNumberByContestId(contestId, 1),
                pageNum, pageSize);
    }

    private <T> Map<String, Object> getResultMap(List<T> records, Long total,
                                                 Integer pageNum, Integer pageSize) {
        return new LinkedHashMap<>(4) {
            {
                put("records", records);
                put("total", total);
                put("pageNum", pageNum);
                put("pageSize", pageSize);
            }
        };
    }

    private String createAdmin(String username) {
        Account account = new Account();
        String password = randomUtil.randomStr(8);
        account.setUsername(username);
        account.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        account.setRole(1);
        accountMapper.insert(account);
        return password;
    }

}
