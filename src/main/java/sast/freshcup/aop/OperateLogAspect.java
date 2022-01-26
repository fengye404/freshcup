package sast.freshcup.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sast.freshcup.annotation.OperateLog;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;
import sast.freshcup.interceptor.AccountInterceptor;
import sast.freshcup.interceptor.RequestInterceptor;
import sast.freshcup.util.CommonUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author: 風楪fy
 * @create: 2022-01-27 01:18
 **/
@Aspect
@Component
public class OperateLogAspect {
    private final static Logger logg = LoggerFactory.getLogger(OperateLogAspect.class);
    private final static Set<String> EXCLUDE_SET;

    static {
        EXCLUDE_SET = new HashSet<>();
        EXCLUDE_SET.add("password");
    }

    @Pointcut("@annotation(sast.freshcup.annotation.OperateLog)")
    public void operateLog() {
    }

    @Around("operateLog()&&@annotation(log)")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint, OperateLog log) throws Throwable {
        Map<String, Object> paramMap = CommonUtil.getRequestParamMap(proceedingJoinPoint, EXCLUDE_SET);
        Object returnValue = proceedingJoinPoint.proceed();
        try {
            Account account = Optional.ofNullable(AccountInterceptor.accountHolder.get()).orElseThrow(() -> {
                throw new LocalRunTimeException(ErrorEnum.NO_LOGIN);
            });

            Optional.ofNullable(RequestInterceptor.requestHolder.get()).ifPresent((preTrack) -> {
                preTrack.setSpendTime(System.currentTimeMillis() - Long.parseLong(preTrack.getSpendTime()) + "ms")
                        .setDescription(log.operDesc())
                        .setParams(paramMap)
                        .setResult(returnValue)
                        .setUser(account);
                logg.info(preTrack.toLogFormat(true));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    @AfterThrowing(pointcut = "operateLog()&&@annotation(log)", throwing = "exception")
    public void throwHandler(JoinPoint joinPoint, OperateLog log, Throwable exception) {
        Map<String, Object> paramMap = CommonUtil.getRequestParamMap(joinPoint, EXCLUDE_SET);
        try {
            Account account = Optional.ofNullable(AccountInterceptor.accountHolder.get()).orElseThrow(() -> {
                throw new LocalRunTimeException(ErrorEnum.NO_LOGIN);
            });

            Optional.ofNullable(RequestInterceptor.requestHolder.get()).ifPresent(preTrack -> {
                preTrack.setSpendTime(System.currentTimeMillis() - Long.parseLong(preTrack.getSpendTime()) + "ms")
                        .setDescription(log.operDesc())
                        .setParams(paramMap)
                        .setResult(exception)
                        .setUser(account);
                logg.info(preTrack.toLogFormat(false));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
