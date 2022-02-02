package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.*;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.*;
import sast.freshcup.service.RedisService;
import sast.freshcup.service.SuperContestService;

import java.util.*;
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

    private final AccountContestManagerMapper accountContestManagerMapper;

    private final ProblemJudgerMapper problemJudgerMapper;

    private final AccountMapper accountMapper;

    private final JudgeMapper judgeMapper;

    private final ResultMapper resultMapper;

    public SuperContestServiceImpl(ContestMapper contestMapper,
                                   ProblemMapper problemMapper,
                                   RedisService redisService, AnswerMapper answerMapper,
                                   AccountContestManagerMapper accountContestManagerMapper,
                                   ProblemJudgerMapper problemJudgerMapper,
                                   AccountMapper accountMapper, JudgeMapper judgeMapper,
                                   ResultMapper resultMapper) {
        this.contestMapper = contestMapper;
        this.problemMapper = problemMapper;
        this.redisService = redisService;
        this.answerMapper = answerMapper;
        this.accountContestManagerMapper = accountContestManagerMapper;
        this.problemJudgerMapper = problemJudgerMapper;
        this.accountMapper = accountMapper;
        this.judgeMapper = judgeMapper;
        this.resultMapper = resultMapper;
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
        LambdaQueryWrapper<Contest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contest::getId, id);
        if (getContestById(id) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        contestMapper.delete(queryWrapper);

        //能够查询到对应的比赛
        resetContest(id);
    }


    /**
     * @param problemIds
     * @param contestId
     * @Description: 给题目进行排序，原地哈希排序
     */
    @Override
    public void problemSort(List<Long> problemIds, Long contestId) {
        if (contestMapper.selectOne(new QueryWrapper<Contest>().eq("id", contestId)) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        List<Problem> problems = new ArrayList<>(problemIds.size());
        for (int i = 0; i < problemIds.size(); i++) {
            QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", problemIds.get(i));
            Problem problem = problemMapper.selectOne(queryWrapper);
            if (problem == null) {
                throw new LocalRunTimeException(ErrorEnum.PROBLEM_NOT_EXIST);
            }
            if (!problem.getContestId().equals(contestId)) {
                throw new LocalRunTimeException(ErrorEnum.CONTEST_INCONSISTENCY);
            }
            problem.setOrderId(i + 1);
            problems.add(problem);
        }
        Set<Long> idSet = new HashSet<>(problemIds);
        if (idSet.size() != problems.size()) {
            throw new LocalRunTimeException(ErrorEnum.PROBLEM_EXIST);
        }
        resetProblemSort(contestId);
        problems.forEach((problemMapper::updateById));
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
     * @Description: 重置比赛信息，账号、提交记录、题目、比赛具体信息保留
     */
    @Override
    public void resetContest(Long id) {
        accountContestManagerMapper.delete(
                new LambdaQueryWrapper<AccountContestManager>().eq(AccountContestManager::getContestId, id)
        );
        problemJudgerMapper.delete(
                new LambdaQueryWrapper<ProblemJudger>().eq(ProblemJudger::getContestId, id)
        );
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
    public Map<String, Object> getAllContest(Integer pageNum, Integer pageSize) {
        Page<Contest> data = contestMapper.selectPage(new Page<>(pageNum, pageSize), null);
        return getResultMap(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }


    /**
     * @param contestId
     * @param pageNum
     * @param pageSize
     * @return
     * @Description: 获取对应比赛题目
     */
    @Override
    public Map<String, Object> getAllProblem(Long contestId, Integer pageNum, Integer pageSize) {
        if (contestMapper.selectOne(new QueryWrapper<Contest>().eq("id", contestId)) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }
        Page<Problem> data = problemMapper.selectPage(new Page<>(pageNum, pageSize),
                new QueryWrapper<Problem>().eq("contest_id", contestId)
                        .orderByAsc("order_id"));
        return getResultMap(data.getRecords(), data.getTotal(), pageNum, pageSize);
    }

    @Override
    public Workbook exportResult(Long contestId) {
        if (contestMapper.selectOne(
                new LambdaQueryWrapper<Contest>()
                        .eq(Contest::getId, contestId)) == null) {
            throw new LocalRunTimeException(ErrorEnum.NO_CONTEST);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("result");
        Row row = sheet.createRow(0);

        row.createCell(0).setCellValue("学号");
        List<Problem> problems = problemMapper.selectList(
                new LambdaQueryWrapper<Problem>()
                        .eq(Problem::getContestId, contestId)
                        .orderByAsc(Problem::getOrderId)
        );
        for (int i = 0; i < problems.size(); i++) {
            row.createCell(i + 1).setCellValue(problems.get(i).getProblemName());
        }
        row.createCell(problems.size() + 1).setCellValue("总分");

        List<Long> uids = answerMapper.getUidsByContestId(contestId);
        for (int i = 0; i < uids.size(); i++) {
            int total = 0;

            //获取学号对应的答案
            List<Answer> answers = answerMapper.selectList(
                    new LambdaQueryWrapper<Answer>()
                            .eq(Answer::getContestId, contestId)
                            .eq(Answer::getUid, uids.get(i))
            );

            row = sheet.createRow(i + 1);
            //获取学号
            row.createCell(0).setCellValue(
                    accountMapper.selectOne(
                            new LambdaQueryWrapper<Account>()
                                    .eq(Account::getUid, uids.get(i))
                    ).getUsername()
            );

            //计算总分
            for (int j = 0; j <= answers.size(); j++) {
                if (j == answers.size()) {
                    row.createCell(problems.size() + 1).setCellValue(String.valueOf(total));
                    break;
                }

                Problem problem = problemMapper.selectOne(
                        new LambdaQueryWrapper<Problem>()
                                .eq(Problem::getId, answers.get(j).getProblemId())
                );
                int orderId = problem.getOrderId();

                Cell cell = row.createCell(orderId);
                if (problem.getType() == 1) {
                    //选择题
                    if (answers.get(j).getContent().equals(problem.getAnswer())) {
                        total += problem.getScore();
                        cell.setCellValue(problem.getScore().toString());
                    } else {
                        cell.setCellValue("0");
                    }
                    //主观题
                } else if (problem.getType() == 0) {
                    Judge judge = judgeMapper.selectOne(
                            new LambdaQueryWrapper<Judge>()
                                    .eq(Judge::getAnswerId, answers.get(j).getId())
                    );
                    cell.setCellValue(judge.getScore().toString());
                    total += judge.getScore();
                } else {
                    throw new LocalRunTimeException(ErrorEnum.NO_PROBLEM);
                }
            }

            resultMapper.insert(new Result(contestId, uids.get(i), total));
        }

        return workbook;
    }

    private <T> Map<String, Object> getResultMap(List<T> records, Long total,
                                                 Integer pageNum, Integer pageSize) {
        return new LinkedHashMap<>() {
            {
                put("records", records);
                put("total", total);
                put("pageNum", pageNum);
                put("pageSize", pageSize);
            }
        };
    }

    /**
     * @param contestId
     * @Description: 重置当前比赛其他题目的顺序
     */
    private void resetProblemSort(Long contestId) {
        List<Problem> problems = problemMapper.selectList(
                new QueryWrapper<Problem>()
                        .eq("contest_id", contestId));
        //使用 UpdateWrapper 更新 orderId 为空值
        problems.forEach(problem -> problemMapper.update(problem,
                new UpdateWrapper<Problem>()
                        .set("order_id", null)
                        .eq("id", problem.getId())
        ));
    }

}
