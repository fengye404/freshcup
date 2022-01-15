package sast.freshcup.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sast.freshcup.response.Result;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:15
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(LocalRunTimeException.class)
    public Result localRunTimeException(LocalRunTimeException e) {
        log.error("异常", e);
        if (e.getErrorEnum() != null) {
            return Result.failure(e.getErrorEnum());
        } else {
            return Result.failure(e.getMessage());
        }
    }
}
