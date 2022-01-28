package sast.freshcup.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-23 22:05
 **/
public interface SuperUserService {

    Map<String, Object> importUserAccount(MultipartFile multipartFile) throws IOException;

    Map<String, Object> getAllContestUser(Long contestId, Integer pageNum, Integer pageSize);

    Map<String, Object> getAllContestUser(Integer pageNum, Integer pageSize);

    void deleteUserById(Long uid);

    void createUser(String username);

}
