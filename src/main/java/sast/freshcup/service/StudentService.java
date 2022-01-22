package sast.freshcup.service;

import sast.freshcup.pojo.ContestVO;

import java.util.List;

/**
 * @author: 風楪fy
 * @create: 2022-01-22 17:43
 **/
public interface StudentService {

    public List<ContestVO> getContestList(Integer pageNum,Integer pageSize);
}
