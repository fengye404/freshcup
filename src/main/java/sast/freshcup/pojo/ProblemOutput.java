package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.Problem;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 21:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemOutput {

    private List<Problem> records;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

}
