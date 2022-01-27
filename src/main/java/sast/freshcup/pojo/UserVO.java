package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 22:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private List<UserSearch> records;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

}
