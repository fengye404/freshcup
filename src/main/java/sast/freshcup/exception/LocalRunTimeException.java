package sast.freshcup.exception;

import lombok.Data;
import sast.freshcup.common.enums.ErrorEnum;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:17
 **/
@Data
public class LocalRunTimeException extends RuntimeException{
    private ErrorEnum errorEnum;

    //默认错误
    public LocalRunTimeException(String message) {
        super(message);
    }

    public LocalRunTimeException(ErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errorEnum = errorEnum;
    }
}
