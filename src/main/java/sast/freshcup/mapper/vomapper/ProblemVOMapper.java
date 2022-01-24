package sast.freshcup.mapper.vomapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sast.freshcup.pojo.ProblemVO;

/**
 * @author: 風楪fy
 * @create: 2022-01-23 05:18
 **/
@Repository
public interface ProblemVOMapper extends BaseMapper<ProblemVO> {
}
