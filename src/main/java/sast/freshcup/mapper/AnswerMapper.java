package sast.freshcup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sast.freshcup.entity.Answer;

import java.util.List;

/**
 * (Answer)表数据库访问层
 *
 * @author 風楪fy
 * @since 2022-01-18 21:08:24
 */
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {

    List<Long> getUidsByContestId(Long contestId);

}
