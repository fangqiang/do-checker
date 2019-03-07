package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.dochecker.annotations.CheckByMethod;
import cn.truthseeker.dochecker.exception.DoCheckException;

import java.lang.reflect.Method;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class CheckByMethodHandler implements CheckHandler<CheckByMethod> {

    /**
     * 1. 方法必须存在
     * 2. 方法返回值必须是boolean类型
     *
     * @param clazz      类
     * @param fieldType  字段值
     * @param fieldName  字段名
     * @param annotation 注解
     * @throws DoCheckException
     */
    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckByMethod annotation) throws DoCheckException {
        Class<?> returnType;
        try {
            String methodName = annotation.value();
            Method declaredMethod = clazz.getDeclaredMethod(methodName);
            returnType = declaredMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new DoCheckException(buildInitErrorMessage("method not found", clazz, fieldType, fieldName, annotation));
        }

        isVerifyTrue(returnType == boolean.class || returnType == Boolean.class,"check method return type should be Boolean",  clazz, fieldType, fieldName, annotation);
    }

    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckByMethod annotation) throws DoCheckException {
        try {
            String methodName = annotation.value();
            Method declaredMethod = clazz.getDeclaredMethod(methodName);
            declaredMethod.setAccessible(true);
            boolean ok = (boolean) declaredMethod.invoke(instance);
            isCheckTrue(ok, clazz, fieldName, fieldValue, annotation);
        } catch (Throwable e) {
            throw new DoCheckException(buildCheckErrorMessage(e.getMessage(), clazz, fieldName, fieldValue, annotation));
        }
    }
}
