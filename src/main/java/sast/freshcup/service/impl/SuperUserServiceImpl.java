package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import sast.freshcup.entity.Account;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.Contest;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.AccountContestManagerMapper;
import sast.freshcup.mapper.AccountMapper;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.pojo.UserOutput;
import sast.freshcup.pojo.UserSearch;
import sast.freshcup.service.SuperUserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 22:05
 **/
@Service
@Slf4j
public class SuperUserServiceImpl implements SuperUserService {

    private final ContestMapper contestMapper;

    private final AccountMapper accountMapper;

    private final AccountContestManagerMapper accountContestManagerMapper;


    public SuperUserServiceImpl(AccountMapper accountMapper,
                                AccountContestManagerMapper accountContestManagerMapper,
                                ContestMapper contestMapper) {
        this.accountMapper = accountMapper;
        this.accountContestManagerMapper = accountContestManagerMapper;
        this.contestMapper = contestMapper;
    }


    @Override
    public Map<String, Object> importUserAccount(MultipartFile multipartFile) throws IOException {
        Map resultMap = new HashMap();
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        Workbook wb = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        int size = sheet.getLastRowNum();
        for (int i = 1; i < size; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            String studentId = cell.getStringCellValue();
            Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>().eq(Account::getUsername, studentId));
            if (account != null) {
                log.info(account.getUsername() + "学生已导入，无需再次导入");
                failure.getAndIncrement();
            } else {
                log.info(studentId + "学生导入成功");
                createUser(studentId);
                success.getAndIncrement();
            }
        }

        resultMap.put("success", success);
        resultMap.put("failure", failure);

        return resultMap;
    }

    @Override
    public UserOutput getAllContestUser(Long contestId, Integer pageNum, Integer pageSize) {
        return new UserOutput(accountMapper.getUsersByContestId(contestId, pageNum, pageSize),
                accountMapper.getUsersNumberByContestId(contestId),
                pageNum, pageSize);
    }

    @Override
    public void attributeContest(AccountContestManager accountContestManager) {
        QueryWrapper<AccountContestManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", accountContestManager.getUid());
        if (accountContestManagerMapper.selectOne(queryWrapper) != null) {
            throw new LocalRunTimeException(ErrorEnum.USER_EXIST);
        }
        QueryWrapper<Contest> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("cid", accountContestManager.getContestId());
        if (contestMapper.selectOne(queryWrapper1) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        QueryWrapper<Account> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("uid", accountContestManager.getUid());
        Account account = accountMapper.selectOne(queryWrapper2);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 0) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        accountContestManagerMapper.insert(accountContestManager);
    }

    @Override
    public void deleteUserById(Long uid) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Account account = accountMapper.selectOne(queryWrapper);
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 0) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }
        accountMapper.deleteById(uid);
    }

    @Override
    public void createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(DigestUtils.md5DigestAsHex(username.getBytes()));
        account.setRole(0);
        accountMapper.insert(account);
    }

}
