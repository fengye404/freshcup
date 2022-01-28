package sast.freshcup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sast.freshcup.entity.Account;
import sast.freshcup.pojo.AccountVO;

import java.util.List;

/**
 * (Account)表数据库访问层
 *
 * @author 風楪fy
 * @since 2022-01-18 21:08:24
 */
@Repository
public interface AccountMapper extends BaseMapper<Account> {

    List<AccountVO> getUsersByContestId(Long contestId, Integer role,
                                        Integer pageNum, Integer pageSize);

    Long getUsersNumberByContestId(Long contestId, Integer role);

}
