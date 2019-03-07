package cn.truthseeker.dochecker.annotations;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能：容器不能为空，至少存在一个元素
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope({Collection.class, Map.class})
public @interface CheckContainerNotEmpty {
}
