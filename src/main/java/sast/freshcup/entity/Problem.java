package sast.freshcup.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Problem)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem implements Serializable {
    private static final long serialVersionUID = 298970394659612485L;

    /**
     * 题目ID
     */
    private Long id;

    /**
     * 比赛ID
     */
    private Long contestId;

    /**
     * 出题人UID
     */
    private Long ownerUid;

    /**
     * 客观题、主观题（0客观题，1主观题）
     */
    private Integer type;

    /**
     * 正则支持多个答案
     */
    private String answer;

    /**
     * 题目内容
     */
    private String desc;

    /**
     * A
     */
    private String opt1;

    /**
     * B
     */
    private String opt2;

    /**
     * C
     */
    private String opt3;

    /**
     * D
     */
    private String opt4;

    /**
     * 分值
     */
    private Integer score;

    /**
     * 在一个比赛中出现的题号
     */
    private Integer order;

    /**
     * 图片Base64，用"#"拼接
     */
    private String pic;

}

