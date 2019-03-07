package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;

/**
 * 功能：用于描述每个Promise注解适用于哪些类型
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Scope {

    Class[] value();
}
