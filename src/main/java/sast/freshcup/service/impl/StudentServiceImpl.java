package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.Contest;
import sast.freshcup.mapper.AccountContestManagerMapper;
import sast.freshcup.mapper.AccountMapper;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.pojo.ContestVO;
import sast.freshcup.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

import static sast.freshcup.interceptor.AccountInterceptor.accountHolder;

/**
 * @author: 風楪fy
 * @create: 2022-01-22 17:44
 **/
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountContestManagerMapper accountContestManagerMapper;

    @Override
    public List<ContestVO> getContestList(Integer pageNum, Integer pageSize) {
        Page<Contest> page = new Page<>(pageNum, pageSize);
        contestMapper.selectPage(page, null);
        List<Contest> records = page.getRecords();
        long total = page.getTotal();
        //用 stream 将 List<Contest> 转化为 List<ContestVO>
        List<ContestVO> collect = records.stream().map((contest) -> {
            ContestVO contestVO = new ContestVO(contest);
            contestVO.setEnable(false);
            AccountContestManager manager = accountContestManagerMapper.selectOne(new QueryWrapper<AccountContestManager>()
                    .eq("contest_id", contestVO.getId())
                    .eq("uid", accountHolder.get().getUid()));
            if (manager != null) {
                contestVO.setEnable(true);
            }
            return contestVO;
        }).collect(Collectors.toList());
        return collect;
    }
}
