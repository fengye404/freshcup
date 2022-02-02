package sast.freshcup.service;

import org.apache.poi.ss.usermodel.Workbook;
import sast.freshcup.entity.Contest;

import java.io.IOException;
import java.util.List;
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

    void problemSort(List<Long> problemIds, Long contestId);

    void answerUpload();

    void resetContest(Long id);

    Contest getContestById(Long id);

    Map<String, Object> getAllContest(Integer pageNum, Integer pageSize);

    Map<String, Object> getAllProblem(Long contestId, Integer pageNum, Integer pageSize);

    Workbook exportResult(Long contestId) throws IOException;

}
