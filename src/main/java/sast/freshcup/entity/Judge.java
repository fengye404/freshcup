package sast.freshcup.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Judge)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judge implements Serializable {
    private static final long serialVersionUID = 569911924798750657L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 答案ID
     */
    private Long answerId;

    /**
     * 阅卷人ID
     */
    private Long judgerId;

    
    private Integer score;

}

