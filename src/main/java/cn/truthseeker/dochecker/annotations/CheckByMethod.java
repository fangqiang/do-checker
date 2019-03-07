package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;

/**
 * 功能：指定校验方法（方法返回值为必须boolean类型）
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope({})
public @interface CheckByMethod {
    String value();
}
