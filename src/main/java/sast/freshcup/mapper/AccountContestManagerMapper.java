package sast.freshcup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sast.freshcup.entity.AccountContestManager;

import java.util.List;

/**
 * (AccountContestManager)表数据库访问层
 *
 * @author 風楪fy
 * @since 2022-01-18 21:08:24
 */
@Repository
public interface AccountContestManagerMapper extends BaseMapper<AccountContestManager> {

    List<Long> getUidsByContestId(Long ContestId);

}
