package sast.freshcup.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sast.freshcup.entity.Answer;
import sast.freshcup.mapper.AnswerMapper;
import sast.freshcup.service.RedisService;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: 風楪fy
 * @create: 2022-01-25 00:29
 **/
@Slf4j
@Component
public class ScheduleTask {
    @Autowired
    private RedisService redisService;
    @Autowired
    private AnswerMapper answerMapper;

    public void updateAnswer() {
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
        collect.forEach((answer -> answerMapper.insert(answer)));
    }
}
