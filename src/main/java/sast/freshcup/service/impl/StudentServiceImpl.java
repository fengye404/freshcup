package sast.freshcup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sast.freshcup.common.constants.RedisKeyConst;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.Contest;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.*;
import sast.freshcup.mapper.cache.ProblemVORepository;
import sast.freshcup.mapper.vomapper.ProblemVOMapper;
import sast.freshcup.mapper.vomapper.SimpleProblemVOMapper;
import sast.freshcup.pojo.ContestVO;
import sast.freshcup.pojo.ProblemVO;
import sast.freshcup.pojo.SimpleProblemVO;
import sast.freshcup.service.RedisService;
import sast.freshcup.service.StudentService;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private SimpleProblemVOMapper simpleProblemVOMapper;
    @Autowired
    private ProblemVOMapper problemVOMapper;
    @Autowired
    private RedisService redisService;

    @Autowired
    private ProblemVORepository problemVORepository;

    @Override
    public Map<String, Object> getContestList(Integer pageNum, Integer pageSize) {
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

        Map<String, Object> res = new HashMap<>();
        res.put("total", total);
        res.put("pageNum", pageNum);
        res.put("pageSize", pageSize);
        res.put("records", collect);
        return res;
    }

    @Override
    public Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize) {
        Page<SimpleProblemVO> page = new Page<>(pageNum, pageSize);
        simpleProblemVOMapper.selectOne(new QueryWrapper<SimpleProblemVO>().eq("contest_id", contestId));
        List<SimpleProblemVO> records = page.getRecords();
        long total = page.getTotal();

        Map<String, Object> res = new HashMap<>();
        res.put("total", total);
        res.put("pageNum", pageNum);
        res.put("pageSize", pageSize);
        res.put("records", records);
        return res;
    }

    @Override
    public ProblemVO getProblemById(Long problemId) {
        //无缓存
        //ProblemVO problemVO = problemVOMapper.selectById(problemId);
        //redis缓存
        ProblemVO problemVO = problemVORepository.getProblemVOById(problemId);
        return problemVO;
    }

    @Override
    public void uploadAnswer(Long contestId, Long problemId, String content) {
        Long uid = accountHolder.get().getUid();
        redisService.set(RedisKeyConst.getAnswerKey(contestId, uid, problemId), content);
    }

    @Override
    public Map<String, Object> getRemainingTime(Long contestId) {
        Contest contest = contestMapper.selectById(contestId);
        LocalDateTime start = contest.getStart();
        LocalDateTime end = contest.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(end) || now.isBefore(start)) {
            throw new LocalRunTimeException(ErrorEnum.NOT_TIME);
        } else {
            Duration between = Duration.between(now, end);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            //转换时区，否则会多8小时
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String formatTime = simpleDateFormat.format(between.toMillis());
            Map<String, Object> res = new HashMap<>();
            res.put("remainingTime", formatTime);
            return res;
        }
    }
}
