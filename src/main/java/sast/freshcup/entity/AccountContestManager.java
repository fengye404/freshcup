package sast.freshcup.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (AccountContestManager)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountContestManager implements Serializable {
    private static final long serialVersionUID = 397178340921898996L;

    
    private Long id;

    /**
     * 比赛ID
     */
    private Long contestId;

    /**
     * 用户UID
     */
    private Long uid;

}

