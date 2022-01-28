package sast.freshcup.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 23:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
public class AccountVO {

    private Long uid;

    private String username;

    private Integer role;

}
