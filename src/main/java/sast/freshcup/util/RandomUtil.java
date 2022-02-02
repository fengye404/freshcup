package sast.freshcup.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @program: freshcup
 * @author: cxy621
 * @create: 2022-01-22 20:31
 **/
@Component
public class RandomUtil {

//    private final AccountMapper accountMapper;
//
//    public RandomUtil(AccountMapper accountMapper) {
//        this.accountMapper = accountMapper;
//    }

    public String randomStr(int length) {
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(str.length());
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 保证不会重复
     * @return 返回随机生成的8位字符串
     *//*
    public String getStr() {
        String username = randomStr();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        do {
            username = randomStr();
        } while (accountMapper.selectOne(queryWrapper) != null);
        return username;
    }*/

}
