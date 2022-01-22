package sast.freshcup.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "比赛名称不能为 null")
    private String name;

    /**
     * 开始时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "比赛名称不能为 null")
    private LocalDateTime start;

    /**
     * 结束时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "比赛名称不能为 null")
    private LocalDateTime end;

    /**
     * 比赛描述
     */
    private String description;

}

