package sast.freshcup.annotation;

import java.lang.annotation.*;

/**
 * @author: 風楪fy
 * @create: 2022-01-27 01:17
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    String operDesc() default "";
}
