package sast.freshcup.mapper;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import sast.freshcup.entity.Account;

/**
 * (Account)表数据库访问层
 *
 * @author 風楪fy
 * @since 2022-01-18 21:08:24
 */
@Repository
public interface AccountMapper extends BaseMapper<Account> {

}
