package sast.freshcup.annotation;

import sast.freshcup.common.enums.AuthEnum;

import java.lang.annotation.*;

/**
 * @author: 風楪fy
 * @create: 2022-01-19 22:50
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthHandle {
    AuthEnum value();
}
