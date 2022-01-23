package sast.freshcup.service;

import sast.freshcup.entity.Contest;
import sast.freshcup.pojo.ContestOutput;
import sast.freshcup.pojo.ProblemOutput;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 12:32
 **/
public interface SuperContestService {

    void createContest(Contest contest);

    void editContest(Contest contest);

    void deleteContest(Long cid);

    void problemSort(Long id, Integer orderId);

    Contest getContestById(Long cid);

    ContestOutput getAllContest(Integer pageNum, Integer pageSize);

    ProblemOutput getAllProblem(Long contestId, Integer pageNum, Integer pageSize);

}
