package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * 功能：约定字符串满足一定格式
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope(String.class)
public @interface CheckStringFormat {

    FormatType[] value();
}
