package sast.freshcup.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "比赛 ID 不能为 null")
    private Long contestId;

    /**
     * 题目ID
     */
    @NotNull(message = "题目 ID 不能为 null")
    private Long problemId;

    /**
     * 评委ID
     */
    @NotNull(message = "评委 ID 不能为 null")
    private Long judgerId;

}

