package sast.freshcup.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import sast.freshcup.annotation.AuthHandle;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.interceptor.AccountInterceptor;

/**
 * @author: 風楪fy
 * @create: 2022-01-20 00:15
 **/
@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Pointcut("@annotation(sast.freshcup.annotation.AuthHandle)")
    public void start() {

    }

    @Before("start()&&@annotation(auth)")
    public Object auth(JoinPoint joinPoint, AuthHandle auth) {
        AuthEnum authEnum = auth.value();
        Account account = AccountInterceptor.accountHolder.get();
        if (!AuthEnum.checkAuth(account, authEnum)) {
            throw new LocalRunTimeException("权限不足");
        }
        return joinPoint;
    }

}
