package sast.freshcup.service;

import sast.freshcup.pojo.ProblemVO;

import java.util.Map;

/**
 * @author: 風楪fy
 * @create: 2022-01-22 17:43
 **/
public interface StudentService {

    Map<String, Object> getContestList(Integer pageNum, Integer pageSize);

    Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize);

    ProblemVO getProblemById(Long problemId);

    void uploadAnswer(Long contestId, Long problemId, String content);

    Map<String, Object> getRemainingTime(Long contestId);

}