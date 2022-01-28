package sast.freshcup.service;

import sast.freshcup.entity.Judge;
import sast.freshcup.entity.Problem;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Acow337
 * @Date: 2022/01/20/20:05
 * @Description:
 */

public interface AdminService {
    String judgeProblem(Judge judge);

    String setProblem(Problem problem);

    String deleteProblem(Long id);

    String updateProblem(Problem problem);

    Map<String, Object> getProblemList(Long contestId, Integer pageNum, Integer pageSize);

    Map<String, Object> getContestList(Integer pageNum, Integer pageSize);

    Problem getProblemInfo(Long id);

}
