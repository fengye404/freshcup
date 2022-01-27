package sast.freshcup.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.Problem;

/**
 * @author: 風楪fy
 * @create: 2022-01-23 01:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem")
public class SimpleProblemVO {

    private Long id;

    private String problemName;

    private Integer orderId;

    public SimpleProblemVO(Problem problem) {
        this.id = problem.getId();
        this.problemName = problem.getProblemName();
        this.orderId = problem.getOrderId();
    }

}
