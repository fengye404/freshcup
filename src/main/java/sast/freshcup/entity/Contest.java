package sast.freshcup.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Contest)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest implements Serializable {
    private static final long serialVersionUID = -74120203438419247L;

    /**
     * 比赛ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 比赛名
     */
    private String name;

    /**
     * 开始时间
     */
    private LocalDateTime start;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 比赛描述
     */
    private String description;

}

