package sast.freshcup.service;

import sast.freshcup.entity.Contest;
import sast.freshcup.pojo.ContestListVO;
import sast.freshcup.pojo.ProblemListVO;

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

    ContestListVO getAllContest(Integer pageNum, Integer pageSize);

    ProblemListVO getAllProblem(Long contestId, Integer pageNum, Integer pageSize);

}
