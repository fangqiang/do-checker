package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;

/**
 * 功能：枚举
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope({String.class,Number.class})
public @interface CheckEnum {

    /**
     * 填写枚举值
     */
    String value() default "";
    String separator() default ",";

    /**
     * 引用枚举类中的值
     */
    Class<? extends Enum> enumType() default DefaultEnum.class; // 枚举的类型
    String enumField() default ""; // 获取枚举中的字段
}
