package sast.freshcup.mapper.vomapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sast.freshcup.pojo.SimpleProblemVO;

/**
 * @author: 風楪fy
 * @create: 2022-01-23 05:20
 **/
@TableName("problem")
@Repository
public interface SimpleProblemVOMapper extends BaseMapper<SimpleProblemVO> {
}
