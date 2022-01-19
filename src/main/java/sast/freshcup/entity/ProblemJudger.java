package sast.freshcup.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (ProblemJudger)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemJudger implements Serializable {
    private static final long serialVersionUID = -91662854793971347L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 比赛ID
     */
    private Long contestId;

    /**
     * 题目ID
     */
    private Long problemId;

    /**
     * 评委ID
     */
    private Long judgerId;

}

