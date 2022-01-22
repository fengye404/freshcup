package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.pojo.ContestOutput;
import sast.freshcup.service.SuperContestService;

import java.util.List;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 12:35
 **/
@Service
@Slf4j
public class SuperContestServiceImpl implements SuperContestService {

    private final ContestMapper contestMapper;

    public SuperContestServiceImpl(ContestMapper contestMapper) {
        this.contestMapper = contestMapper;
    }

    @Override
    public void createContest(Contest contest) {
        if (contest.getStart().compareTo(contest.getEnd()) > 0) {
            throw new LocalRunTimeException(ErrorEnum.DATE_ERROR);
        }
        contestMapper.insert(contest);
    }

    @Override
    public void editContest(Contest contest) {
        //判断 start 和 end 两者的时间
        if (contest.getStart().compareTo(contest.getEnd()) > 0) {
            throw new LocalRunTimeException(ErrorEnum.DATE_ERROR);
        }
        if (getContestById(contest.getCid()) != null) {
            contestMapper.updateById(contest);
        }
    }

    @Override
    public void deleteContest(Long cid) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", cid);
        if (getContestById(cid) != null) {
            contestMapper.delete(queryWrapper);
        }
    }

    @Override
    public Contest getContestById(Long cid) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", cid);
        Contest contest = contestMapper.selectOne(queryWrapper);
        if (contest == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        return contest;
    }

    @Override
    public ContestOutput getAllContest(Integer pageNum, Integer pageSize) {
        Page<Contest> page = new Page<>(pageNum, pageSize);
        Page<Contest> data = contestMapper.selectPage(page, null);
        Long count = data.getTotal();
        return new ContestOutput(data.getRecords(),
                count, pageNum, pageSize);
    }

}
