package cn.truthseeker.dochecker.core;


import cn.truthseeker.container.safe.Maps;
import cn.truthseeker.container.safe.NoneEmptyList;
import cn.truthseeker.container.safe.NoneEmptyMap;
import cn.truthseeker.dochecker.exception.DoCheckException;
import cn.truthseeker.dochecker.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/5
 */
public class DoChecker {

    private final Logger LOG = LoggerFactory.getLogger(DoChecker.class);

    private HandlerRepo handlerRepo = new HandlerRepo();

    private static DoChecker instance = new DoChecker();

    public static DoChecker getInstance() {
        return instance;
    }

    private DoChecker() {
    }

    private NoneEmptyMap<Class, NoneEmptyMap<Field, List<Annotation>>> metaCache = new NoneEmptyMap();

    /**
     * 注册一个类
     *
     * @param clazz
     * @throws DoCheckException
     */
    public synchronized void registerCheckableClass(Class clazz) throws DoCheckException {
        Objects.requireNonNull(clazz,"class can't be null");
        try {
            if (metaCache.containsKey(clazz)) {
                return;
            }
            NoneEmptyMap<Field, List<Annotation>> fieldListMap = verifyCheckableClass(clazz);

            if (!fieldListMap.isEmpty()) {
                metaCache.put(clazz, fieldListMap);
                LOG.info("register class `{}`", clazz.getCanonicalName());
            }

        } catch (Throwable e) {
            LOG.error("", e);
            throw e;
        }
    }

    /**
     * 注册多个类
     *
     * @param clazzs
     * @throws DoCheckException
     */
    public synchronized void registerCheckableClass(Collection<Class<?>> clazzs) throws DoCheckException {
        Objects.requireNonNull(clazzs,"classes can't be null");
        for (Class clazz : clazzs) {
            registerCheckableClass(clazz);
        }
    }

    /**
     * 扫面目标package下带有Checkable注解的类，检查注解配置是否合法
     *
     * @param packageName
     * @throws DoCheckException
     */
    public synchronized void registerCheckableClassByPackage(String packageName) throws DoCheckException {
        Objects.requireNonNull(packageName,"package can't be null");
        registerCheckableClass(ReflectUtil.getAllClass(packageName));
    }

    public void registerAndCheck(Object instance) throws DoCheckException {
        Objects.requireNonNull(instance,"checked object can't be null");
        registerCheckableClass(instance.getClass());
        checkOrException(instance);
    }

    /**
     * 检查所有字段值是否满足注解的限制
     *
     * @param instance
     * @throws DoCheckException
     */
    public void checkOrException(Object instance) throws DoCheckException {
        Objects.requireNonNull(instance,"checked object can't be null");
        if (metaCache.containsKey(instance.getClass())) {
            checkOrException(instance, ReflectUtil.getAllFieldsIncludeInherited(instance));
        }
    }

    /**
     * 检查指定字段值是否满足注解的限制
     *
     * @param instance
     * @throws DoCheckException
     */
    public void checkOrException(Object instance, Set<String> checkFields) throws DoCheckException {
        Objects.requireNonNull(instance,"checked object can't be null");

        if (checkFields.isEmpty()) {
            return;
        }

        Optional<NoneEmptyMap<Field, List<Annotation>>> opt = metaCache.getNullable(instance.getClass());
        opt.ifPresent(fieldListMap -> fieldListMap
                .filterByKey(k -> checkFields.contains(k.getName()))
                .forEach((k, v) -> checkEveryField(instance, k, v)));
    }

    private void checkEveryField(Object instance, Field field, List<Annotation> annotations) {
        Class<?> clazz = instance.getClass();
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        Object fieldValue = ReflectUtil.getFieldValue(instance, field);

        annotations.forEach(annotation -> handlerRepo.verifyValue(instance, clazz, fieldType, fieldName, fieldValue, annotation));
    }

    private NoneEmptyMap<Field, List<Annotation>> verifyCheckableClass(Class clazz) throws DoCheckException {
        List<Field> fields = ReflectUtil.getAllFieldsIncludeInherited(clazz);
        Map<Field, List<Annotation>> fieldListMap = Maps.listToMap(fields, field -> getValidAnnotation(clazz, field));
        return NoneEmptyMap.ofIgnoreEmpty(fieldListMap);
    }

    private List<Annotation> getValidAnnotation(Class clazz, Field field) {
        // 找出注册过的注解
        Annotation[] allAnnotation = ReflectUtil.getAllAnnotation(field);
        // 1、删掉没有注册的注解。  2、校验每个注解
        NoneEmptyList<Annotation> annotations = NoneEmptyList.ofIgnoreEmpty(allAnnotation);
        annotations.removeIf(annotation -> !handlerRepo.contains(annotation.annotationType()));
        annotations.forEach(annotation -> handlerRepo.verifyUsage(clazz, field.getType(), field.getName(), annotation));
        return annotations;
    }

    Map<Class, NoneEmptyMap<Field, List<Annotation>>> getMetaCache() {
        return metaCache;
    }

    void cleanForTest() {
        metaCache.clear();
    }
}