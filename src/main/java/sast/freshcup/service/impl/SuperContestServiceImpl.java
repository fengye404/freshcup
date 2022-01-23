package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.entity.Problem;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.mapper.ProblemMapper;
import sast.freshcup.pojo.ContestOutput;
import sast.freshcup.pojo.ProblemOutput;
import sast.freshcup.service.SuperContestService;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 12:35
 **/
@Service
@Slf4j
public class SuperContestServiceImpl implements SuperContestService {

    private final ContestMapper contestMapper;

    private final ProblemMapper problemMapper;

    public SuperContestServiceImpl(ContestMapper contestMapper, ProblemMapper problemMapper) {
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
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
        if (getContestById(contest.getCid()) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        contestMapper.updateById(contest);
    }

    @Override
    public void deleteContest(Long cid) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", cid);
        if (getContestById(cid) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        contestMapper.delete(queryWrapper);
    }

    @Override
    public void problemSort(Long id, Integer orderId) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Problem problem = problemMapper.selectOne(queryWrapper);
        if (problem == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_PROBLEM);
        }
        problem.setOrderId(orderId);
        problemMapper.updateById(problem);
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
        return new ContestOutput(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

    @Override
    public ProblemOutput getAllProblem(Long contestId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contest_id", contestId).orderByAsc("order_id");
        Page<Problem> page = new Page<>(pageNum, pageSize);
        Page<Problem> data = problemMapper.selectPage(page, queryWrapper);
        return new ProblemOutput(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

}
