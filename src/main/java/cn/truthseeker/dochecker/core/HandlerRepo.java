package cn.truthseeker.dochecker.core;

import cn.truthseeker.container.SimpleCache;
import cn.truthseeker.container.safe.NoneEmptyMap;
import cn.truthseeker.dochecker.annotations.*;
import cn.truthseeker.dochecker.annotations.handler.*;
import cn.truthseeker.dochecker.exception.DoCheckException;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class HandlerRepo {

    private final Logger LOG = LoggerFactory.getLogger(HandlerRepo.class);

    private NoneEmptyMap<Class, CheckHandler> cache = new NoneEmptyMap();

    HandlerRepo() {
        cache.put(CheckNotNull.class, new NotNullHandler());
        cache.put(CheckEnum.class, new EnumHandler());
        cache.put(CheckContainerNotEmpty.class, new ContainerNotEmptyHandler());
        cache.put(CheckExpress.class, new ExpressHandler());
        cache.put(CheckStringFormat.class, new StringFormatHandler());
        cache.put(CheckByMethod.class, new CheckByMethodHandler());
    }

    boolean contains(Class clazz) {
        return cache.containsKey(clazz);
    }

    /**
     * 校验注解使用是否符合规范
     *
     * @param clazz
     * @param fieldType
     * @param fieldName
     * @param annotation
     * @throws DoCheckException
     */
    public void verifyUsage(Class clazz, Class fieldType, String fieldName, Annotation annotation) throws DoCheckException {
        Optional<CheckHandler> checkHandler = cache.getNullable(annotation.annotationType());
        checkHandler.ifPresent(handler->handler.verifyUsage(clazz, fieldType, fieldName, annotation));
    }

    /**
     * 校验字段值，是否满足注解的要求
     *
     * @param instance
     * @param clazz
     * @param fieldType
     * @param fieldName
     * @param fieldValue
     * @param annotation
     * @throws DoCheckException
     */
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, Annotation annotation) throws DoCheckException {
        LOG.debug("checkAnnotation: {}.{}={} [{}]", clazz.getSimpleName(), fieldName, fieldValue, annotation);
        Optional<CheckHandler> checkHandler = cache.getNullable(annotation.annotationType());
        checkHandler.ifPresent(handler->handler.verifyValue(instance, clazz, fieldType, fieldName, fieldValue, annotation));
    }
}
