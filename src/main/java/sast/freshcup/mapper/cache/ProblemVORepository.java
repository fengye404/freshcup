package sast.freshcup.mapper.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sast.freshcup.mapper.vomapper.ProblemVOMapper;
import sast.freshcup.pojo.ProblemVO;
import sast.freshcup.service.RedisService;

/**
 * @author: 風楪fy
 * @create: 2022-01-25 14:39
 **/
@Repository
public class ProblemVORepository extends AbstractRedisCacheRepository<String, ProblemVO> {
    private static final ProblemVO NULL_THING = new ProblemVO();

    @Autowired
    private ProblemVOMapper problemVOMapper;

    @Autowired
    public ProblemVORepository(RedisService redisService) {
        super(redisService, 30 * 60, "PROBLEM:");
    }

    public ProblemVO getProblemVOById(Long problemId) {
        String stringId = problemId.toString();
        return getCache(stringId);
    }


    @Override
    protected ProblemVO nullThing() {
        return NULL_THING;
    }

    @Override
    protected ProblemVO getIfAbsent(String key) {
        return problemVOMapper.selectById(key);
    }
}
