package sast.freshcup.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-29 16:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
public class AccountVO {

    @TableId(type = IdType.AUTO)
    private Long uid;

    private String username;

    private Integer role;

}
