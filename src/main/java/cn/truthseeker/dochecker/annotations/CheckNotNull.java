package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;

/**
 * 功能：约定对象不能为null
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope(Object.class)
public @interface CheckNotNull {
}
