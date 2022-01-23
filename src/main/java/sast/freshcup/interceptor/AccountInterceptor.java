package sast.freshcup.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.mapper.AccountMapper;
import sast.freshcup.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: 風楪fy
 * @create: 2022-01-19 16:14
 **/
@Component
@Slf4j
public class AccountInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private final AccountMapper accountMapper;

    public static ThreadLocal<Account> accountHolder = new ThreadLocal<>();

    public AccountInterceptor(JwtUtil jwtUtil, AccountMapper accountMapper) {
        this.jwtUtil = jwtUtil;
        this.accountMapper = accountMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        log.info("进入触发器");
        String token = request.getHeader("TOKEN");
        if (!StringUtils.hasLength(token)) {
            throw new LocalRunTimeException(ErrorEnum.TOKEN_ERROR);
        }
        Account account = jwtUtil.getAccount(token);
        if (account != null) {
            //登录过期
            if(jwtUtil.isExpired(account)){
                throw new LocalRunTimeException("登录过期");
            }
            Account accountFromDB = accountMapper.selectById(account.getUid());
            //判断是否是已知用户
            if (accountFromDB != null) {
                accountHolder.set(account);
                //更新redis中的token持续时间
                jwtUtil.reFreshToken(account);
                return true;
            }
        }
        throw new LocalRunTimeException(ErrorEnum.TOKEN_ERROR);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        accountHolder.remove();
    }
}
