package sast.freshcup.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 風楪fy
 * @create: 2022-01-23 05:16
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem")
public class ProblemVO {

    private Integer id;

    private Integer contestId;

    private String problemName;

    private Integer type;

    private String description;

    private String opt1;

    private String opt2;

    private String opt3;

    private String opt4;

    private Integer score;

    private Integer orderId;

    private String pic;
}
