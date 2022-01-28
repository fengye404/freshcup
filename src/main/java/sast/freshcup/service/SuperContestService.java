package sast.freshcup.service;

import sast.freshcup.entity.Contest;

import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 12:32
 **/
public interface SuperContestService {

    void createContest(Contest contest);

    void editContest(Contest contest);

    void deleteContest(Long id);

    void problemSort(Long id, Integer orderId);

    void answerUpload();

    Contest getContestById(Long id);

    Map<String, Object> getAllContest(Integer pageNum, Integer pageSize);

    Map<String, Object> getAllProblem(Long contestId, Integer pageNum, Integer pageSize);

}
