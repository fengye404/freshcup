package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 23:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearch {

    private Long uid;

    private String username;

    private Long contestId;

}
