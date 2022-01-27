package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Answer;
import sast.freshcup.entity.Contest;
import sast.freshcup.entity.Problem;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.AnswerMapper;
import sast.freshcup.mapper.ContestMapper;
import sast.freshcup.mapper.ProblemMapper;
import sast.freshcup.pojo.ContestListVO;
import sast.freshcup.pojo.ProblemListVO;
import sast.freshcup.service.RedisService;
import sast.freshcup.service.SuperContestService;

import java.util.Set;
import java.util.stream.Collectors;

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

    private final RedisService redisService;

    private final AnswerMapper answerMapper;

    public SuperContestServiceImpl(ContestMapper contestMapper, ProblemMapper problemMapper, RedisService redisService, AnswerMapper answerMapper) {
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
        this.redisService = redisService;
        this.answerMapper = answerMapper;
    }

    /**
     * @param contest
     * @Description: 创建比赛
     */
    @Override
    public void createContest(Contest contest) {
        if (contest.getStart().compareTo(contest.getEnd()) > 0) {
            throw new LocalRunTimeException(ErrorEnum.DATE_ERROR);
        }
        contestMapper.insert(contest);
    }

    /**
     * @param contest
     * @Description: 编辑比赛
     */
    @Override
    public void editContest(Contest contest) {
        //判断 start 和 end 两者的时间
        if (contest.getStart().compareTo(contest.getEnd()) > 0) {
            throw new LocalRunTimeException(ErrorEnum.DATE_ERROR);
        }
        if (getContestById(contest.getId()) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        contestMapper.updateById(contest);
    }

    /**
     * @param id
     * @Description: 删除比赛
     */
    @Override
    public void deleteContest(Long id) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        if (getContestById(id) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        contestMapper.delete(queryWrapper);
    }

    /**
     * @param id
     * @param orderId
     * @Description: 给题目排序
     */
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
    public void answerUpload() {
        Set<String> keys = redisService.getKeys("ANSWER");
        //stream操作将Set<String> 转化为 Set<Answer>
        Set<Answer> collect = keys.stream().map((key) -> {
            Answer answer = new Answer();
            answer.setContent((String) redisService.get(key));
            //"FC:contestId-ANSWER:uid:problemId"
            String[] split = key.split(":");
            answer.setUid(Long.getLong(split[2]));
            answer.setProblemId(Long.getLong(split[3]));
            answer.setContestId(Long.getLong(split[1].split("-")[0]));
            return answer;
        }).collect(Collectors.toSet());
        collect.forEach((answerMapper::insert));
    }

    /**
     * @param id
     * @return
     * @Description: 获取比赛信息
     */
    @Override
    public Contest getContestById(Long id) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Contest contest = contestMapper.selectOne(queryWrapper);
        if (contest == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        return contest;
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 获取所有比赛
     */
    @Override
    public ContestListVO getAllContest(Integer pageNum, Integer pageSize) {
        Page<Contest> page = new Page<>(pageNum, pageSize);
        Page<Contest> data = contestMapper.selectPage(page, null);
        return new ContestListVO(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }


    /**
     * @param contestId
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 获取对应比赛题目
     */
    @Override
    public ProblemListVO getAllProblem(Long contestId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contest_id", contestId).orderByAsc("order_id");
        Page<Problem> page = new Page<>(pageNum, pageSize);
        Page<Problem> data = problemMapper.selectPage(page, queryWrapper);
        return new ProblemListVO(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

}
