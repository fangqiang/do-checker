package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.container.safe.Collections2;
import cn.truthseeker.dochecker.annotations.Scope;
import cn.truthseeker.dochecker.exception.DoCheckException;
import com.alibaba.fastjson.JSON;

import java.lang.annotation.Annotation;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public interface CheckHandler<T extends Annotation> {

    default void verifyUsage(Class clazz, Class fieldType, String fieldName, T annotation) throws DoCheckException {
        checkScope(clazz, fieldType, fieldName, annotation);
        verifyUsage2(clazz, fieldType, fieldName, annotation);
    }

    /**
     * 检查字段类型是否和注解支持的类型匹配
     *
     * @param clazz
     * @param fieldType
     * @param fieldName
     * @param annotation
     * @throws DoCheckException
     */
    default void checkScope(Class clazz, Class fieldType, String fieldName, T annotation) throws DoCheckException{
        Scope scope = annotation.annotationType().getAnnotation(Scope.class);
        Class[] types = scope.value();
        if(types.length != 0){
            boolean ok = Collections2.anySatisfied(types, type -> type.isAssignableFrom(fieldType));
            if(!ok) {
                throw new DoCheckException(buildInitErrorMessage(String.format("only support data type `%s`", JSON.toJSONString(types)), clazz, fieldType, fieldName, annotation));
            }
        }
    }

    /**
     * 启动时检查每个当前注解的配置是否合法
     *
     * @param clazz      类
     * @param fieldType  字段值
     * @param fieldName  字段名
     * @param annotation 注解
     * @return
     */
    void verifyUsage2(Class clazz, Class fieldType, String fieldName, T annotation) throws DoCheckException;


    /**
     * 运行时检查每个字段的值是否符合该注解要求
     *
     * @param instance   对象
     * @param clazz      检查的类
     * @param fieldType  检查的字段
     * @param fieldName  检查的字段名字
     * @param fieldValue 字段值
     * @param annotation 注解
     * @return
     */
    void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, T annotation) throws DoCheckException;

    default void isVerifyTrue(boolean bool, String message, Class clazz, Class fieldType, String fieldName, T annotation){
        if(!bool){
            throw new DoCheckException(buildInitErrorMessage(message, clazz, fieldType, fieldName, annotation));
        }
    }

    default void isCheckTrue(boolean bool, Class clazz, String fieldName, Object fieldValue, T annotation){
        if(!bool){
            throw new DoCheckException(buildCheckErrorMessage("annotation check failed", clazz, fieldName, fieldValue, annotation));
        }
    }

    default String buildInitErrorMessage(String message, Class clazz, Class fieldType, String fieldName, T annotation) {
        return String.format("%s, field_type=[%s.%s(%s)], annotation=[%s]", message, clazz.getSimpleName(), fieldName, fieldType.getSimpleName(), annotation);
    }

    default String buildCheckErrorMessage(String message, Class clazz, String fieldName, Object fieldValue, T annotation) {
        return String.format("%s, field_value:[%s.%s=%s], annotation:[%s]", message, clazz.getSimpleName(), fieldName, fieldValue, annotation);
    }
}
