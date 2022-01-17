package sast.freshcup.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Result)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private static final long serialVersionUID = -30626510851709404L;

    
    private Long id;

    /**
     * 比赛ID
     */
    private Long contestId;

    /**
     * 答题人UID
     */
    private Long uid;

    /**
     * 总分
     */
    private Integer totalScore;

}

