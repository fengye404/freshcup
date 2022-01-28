package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.AccountContestManagerMapper;
import sast.freshcup.mapper.AccountMapper;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.mapper.vomapper.AccountVOMapper;
import sast.freshcup.pojo.AccountVO;
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

    private final AccountVOMapper accountVOMapper;

    private final AccountContestManagerMapper accountContestManagerMapper;


    public SuperUserServiceImpl(AccountMapper accountMapper,
                                AccountContestManagerMapper accountContestManagerMapper,
                                ContestMapper contestMapper, AccountVOMapper accountVOMapper) {
        this.accountMapper = accountMapper;
        this.accountContestManagerMapper = accountContestManagerMapper;
        this.contestMapper = contestMapper;
        this.accountVOMapper = accountVOMapper;
    }

    /**
     * @param multipartFile
     * @return
     * @throws IOException
     * @Description: 导入学生账号
     */
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

    /**
     * @param contestId
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 如果没有输入比赛id就获取所有学生信息，否则只获取对应比赛学生
     */
    @Override
    public Map<String, Object> getAllContestUser(Long contestId, Integer pageNum, Integer pageSize) {
        return getResultMap(accountMapper.getUsersByContestId(contestId, 0, pageNum, pageSize),
                accountMapper.getUsersNumberByContestId(contestId, 0),
                pageNum, pageSize);
    }

    @Override
    public Map<String, Object> getAllContestUser(Integer pageNum, Integer pageSize) {
        Page<AccountVO> data = accountVOMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<AccountVO>().eq("role", 0)
        );
        return getResultMap(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

    /**
     * @param uid
     * @Description: 删除学生
     */
    @Override
    public void deleteUserById(Long uid) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("uid", uid));
        if (account == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_USER);
        }
        if (account.getRole() != 0) {
            throw new LocalRunTimeException(ErrorEnum.ROLE_ERROR);
        }

        accountMapper.deleteById(uid);
    }

    /**
     * @param username
     * @Description: 手动创建学生
     */
    @Override
    public void createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(DigestUtils.md5DigestAsHex(username.getBytes()));
        account.setRole(0);
        accountMapper.insert(account);
    }

    private <T> Map<String, Object> getResultMap(List<T> records, Long total,
                                                 Integer pageNum, Integer pageSize) {
        return new HashMap<>() {
            {
                put("records", records);
                put("total", total);
                put("pageNum", pageNum);
                put("pageSize", pageSize);
            }
        };
    }

}
