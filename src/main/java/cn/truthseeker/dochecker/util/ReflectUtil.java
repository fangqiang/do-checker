package cn.truthseeker.dochecker.util;

import cn.truthseeker.dochecker.exception.DoCheckException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class ReflectUtil {

    public static Set<Class<?>> getAllClass(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class);
    }

    public static Annotation[] getAllAnnotation(Field field) {
        return field.getAnnotations();
    }

    public static Object getFieldValue(Object instance, String field) throws DoCheckException {
        try {
            Field declaredFields = instance.getClass().getDeclaredField(field);
            return ReflectUtil.getFieldValue(instance, declaredFields);
        } catch (NoSuchFieldException e) {
            throw new DoCheckException(e.getMessage());
        }
    }

    public static Object getFieldValue(Object instance, Field field) throws DoCheckException {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new DoCheckException(e.getMessage());
        }
    }

    public static Set<String> getAllFieldsIncludeInherited(Object instance) {
        return getAllFieldsIncludeInherited(instance.getClass()).stream()
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    public static List<Field> getAllFieldsIncludeInherited(Class clazz) {
        // 用于继承时字段去重
        Set<String> fieldSet = new HashSet<>();
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (fieldSet.add(field.getName())) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static boolean isPrimaryType(Object instance) {
        return instance instanceof String
                || instance instanceof Number
                || instance instanceof Boolean
                || instance instanceof Date;
    }

    public static boolean isCollectionType(Class fieldType) {
        return Collection.class.isAssignableFrom(fieldType);
    }

    public static boolean isMapType(Class fieldType) {
        return Map.class.isAssignableFrom(fieldType);
    }

    public static boolean isStringType(Class fieldType) {
        return fieldType == String.class;
    }

    public static boolean isNumericType(Class fieldType) {
        return fieldType.getSuperclass() == Number.class;
    }

    public static Object invoke(Object instance, String methodName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method declaredMethod = instance.getClass().getDeclaredMethod(methodName);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(instance);
    }
}
