package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.Account;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 15:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOutput {

    private List<Account> records;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

}
