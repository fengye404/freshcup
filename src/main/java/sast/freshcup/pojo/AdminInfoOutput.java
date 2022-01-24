package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.AccountContestManager;
import sast.freshcup.entity.ProblemJudger;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 15:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoOutput {

    private List<AccountContestManager> accountInfos;

    private List<ProblemJudger> problemInfos;

}
