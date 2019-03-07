package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;

/**
 * 功能：判断是否满足表达式
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope({})
public @interface CheckExpress {
    String value();
}
