package sast.freshcup.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 比赛ID
     */
    @NotNull(message = "比赛 id 不能为 null")
    private Long contestId;

    /**
     * 用户UID
     */
    @NotNull(message = "用户 uid 不能为 null")
    private Long uid;

    public AccountContestManager(Long contestId, Long uid) {
        this.contestId = contestId;
        this.uid = uid;
    }

}

