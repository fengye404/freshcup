package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.entity.Judge;
import sast.freshcup.entity.Problem;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.mapper.JudgeMapper;
import sast.freshcup.mapper.ProblemMapper;
import sast.freshcup.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Acow337
 * @Date: 2022/01/20/20:12
 * @Description:
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    JudgeMapper judgeMapper;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    ContestMapper contestMapper;

    @Override
    public String judgeProblem(Judge judge) {
        Long judgerId = judge.getJudgerId();
        Long answerId = judge.getAnswerId();

        if (judgerId == null || answerId == null) {
            throw new LocalRunTimeException(ErrorEnum.PARAMS_LOSS);
        }

        Judge judgeTest = judgeMapper.selectOne(new QueryWrapper<Judge>().eq("judger_id", judgerId).eq("answer_id", answerId));
        if (judgeTest != null) {
            throw new LocalRunTimeException(ErrorEnum.ANSWER_JUDGE);
        }
        judgeMapper.insert(judge);

        return "ok";
    }

    @Override
    public String setProblem(Problem problem) {
        problemMapper.insert(problem);
        return "ok";
    }

    @Override
    public String deleteProblem(Long id) {

        Problem problem = problemMapper.selectOne(new QueryWrapper<Problem>().eq("id", id));
        if (problem == null) {
            throw new LocalRunTimeException(ErrorEnum.PROBLEM_NOT_EXIST);
        }

        problemMapper.delete(new QueryWrapper<Problem>().eq("id", id));

        return "ok";
    }

    @Override
    public Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize) {

        Page<Problem> studentPage = new Page<>(pageNum, pageSize);
        Page<Problem> problemPage = problemMapper.selectPage(studentPage, new QueryWrapper<Problem>().eq("contest_id", contestId));
        return getResultMap(problemPage.getRecords(), problemPage.getTotal(), pageNum, pageSize);
    }

    @Override
    public Map<String, Object> getContestList(Integer pageNum, Integer pageSize) {

        Page<Contest> contestPage = contestMapper.selectPage(new Page<Contest>(pageNum, pageSize), new QueryWrapper<Contest>());
        return getResultMap(contestPage.getRecords(), contestPage.getTotal(), pageNum, pageSize);
    }

    @Override
    public Problem getProblemInfo(Long id) {

        Problem problem = problemMapper.selectOne(new QueryWrapper<Problem>().eq("id", id));
        if (problem == null) {
            throw new LocalRunTimeException(ErrorEnum.PROBLEM_NOT_EXIST);
        }

        return problem;
    }


    @Override
    public String updateProblem(Problem problem) {

        if (problem == null) {
            throw new LocalRunTimeException(ErrorEnum.PARAMS_LOSS);
        }

        int result = problemMapper.update(problem, new UpdateWrapper<Problem>().eq("id", problem.getId()));

        if (result == 0) {
            throw new LocalRunTimeException(ErrorEnum.PROBLEM_NOT_EXIST);
        }

        return "ok";
    }

    public <T> Map<String, Object> getResultMap(List<T> objects, Long num, Integer pageNum, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("records", objects);
        resultMap.put("total", num);
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        return resultMap;
    }

}
