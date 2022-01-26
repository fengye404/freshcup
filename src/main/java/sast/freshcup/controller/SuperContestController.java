package sast.freshcup.controller;

import org.springframework.web.bind.annotation.*;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Contest;
import sast.freshcup.pojo.ContestOutput;
import sast.freshcup.pojo.ProblemOutput;
import sast.freshcup.service.SuperContestService;

import javax.validation.Valid;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:47
 **/
@RestController
@RequestMapping("superadmin/contest")
@AuthHandle(AuthEnum.ADMIN)
public class SuperContestController {

    private final SuperContestService superContestService;

    public SuperContestController(SuperContestService superContestService) {
        this.superContestService = superContestService;
    }

    @PostMapping("create")
    public String createContest(@RequestBody @Valid Contest contest) {
        superContestService.createContest(contest);
        return "success";
    }

    @PostMapping("edit")
    public String editContest(@RequestBody @Valid Contest contest) {
        superContestService.editContest(contest);
        return "success";
    }

    @PostMapping("delete")
    public String deleteContest(Long id) {
        superContestService.deleteContest(id);
        return "success";
    }

    @GetMapping("info")
    public Contest getContest(Long id) {
        return superContestService.getContestById(id);
    }

    @GetMapping("all")
    public ContestOutput getAllContest(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllContest(pageNum, pageSize);
    }

    @PostMapping("sort")
    public String problemSort(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "orderId") Integer orderId) {
        superContestService.problemSort(id, orderId);
        return "success";
    }

    @GetMapping("problem")
    public ProblemOutput getAllProblem(@RequestParam(name = "contestId") Long contestId,
                                       @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return superContestService.getAllProblem(contestId, pageNum, pageSize);
    }

}
