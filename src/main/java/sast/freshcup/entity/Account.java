package sast.freshcup.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long uid;

    
    private String username;

    
    private String password;

    /**
     * student/admin/superadmin
     */
    private String role;

}

