package sast.freshcup.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Answer)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {

    private static final long serialVersionUID = 563331024113391256L;

    /**
     * 答案ID
     */
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
     * 答题人UID
     */
    private Long uid;

    /**
     * 答案
     */
    private String content;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

