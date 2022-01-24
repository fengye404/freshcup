package sast.freshcup.service;

import sast.freshcup.pojo.ProblemVO;

import java.time.Duration;
import java.util.Map;

/**
 * @author: 風楪fy
 * @create: 2022-01-22 17:43
 **/
public interface StudentService {

    public Map<String, Object> getContestList(Integer pageNum, Integer pageSize);

    public Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize);

    public ProblemVO getProblemById(Long problemId);

    public void uploadAnswer(Long contestId, Long problemId, String content);

    public Map<String,Object> getRemainingTime(Long contestId);
}