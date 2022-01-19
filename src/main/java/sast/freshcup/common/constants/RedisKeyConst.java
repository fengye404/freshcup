package sast.freshcup.common.constants;

import sast.freshcup.entity.Account;

/**
 * @author: 風楪fy
 * @create: 2022-01-20 01:19
 **/
public class RedisKeyConst {
    public static String getTokenKey(Account account) {
        return "TOKEN:" + account.getUsername();
    }
}
