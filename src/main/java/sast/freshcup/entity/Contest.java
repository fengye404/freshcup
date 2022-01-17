package sast.freshcup.entity;

import java.util.Date;
import java.io.Serializable;
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
    private Long cid;

    /**
     * 比赛名
     */
    private String name;

    /**
     * 开始时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

}

