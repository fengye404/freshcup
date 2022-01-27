package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.Contest;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 00:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestListVO {

    private List<Contest> records;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

}
