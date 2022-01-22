package sast.freshcup.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * (Account)实体类
 *
 * @author 風楪fy
 * @since 2022-01-17 17:47:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    private static final long serialVersionUID = 532960928738689155L;

    /**
     * 用户UID
     */
    @TableId(type = IdType.AUTO)
    private Long uid;

    @NotNull(message = "用户名不能为 null")
    private String username;

    @NotNull(message = "密码不能为 null")
    private String password;

    /**
     * student/admin/superadmin
     * 0,1,2
     */
    private Integer role;

    public Account(String username, String password, Integer role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

