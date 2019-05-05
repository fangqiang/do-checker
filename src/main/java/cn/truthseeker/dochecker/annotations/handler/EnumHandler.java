package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.container.SimpleCache;
import cn.truthseeker.container.safe.NoneEmptySet;
import cn.truthseeker.util.Emptys;
import cn.truthseeker.util.Utils;
import cn.truthseeker.dochecker.annotations.CheckEnum;
import cn.truthseeker.dochecker.annotations.DefaultEnum;
import cn.truthseeker.dochecker.exception.DoCheckException;
import cn.truthseeker.dochecker.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class EnumHandler implements CheckHandler<CheckEnum> {

    SimpleCache<Annotation, NoneEmptySet<Object>> enumCache = new SimpleCache<>();

    /**
     * 1. value，enumType必须存在其一
     * 2. 如果指定了value
     *      2.1 separator不能为空（默认为`,`）
     *      2.2 value中的内容必须和字段类型一致
     *      2.3 value中的枚举个数必须大于1个
     *      2.4 缓存value中的枚举值(数值类型全按照Double缓存)
     * 3. 如果指定了enumType
     *      3.1 enumField为空
     *          3.1.1 字段类型必须是string
     *          3.1.2 缓存枚举值
     *      3.2 enumField不为空
     *          3.2.1 enumField字段必须存在
     *          3.2.2 enumField的类型必须和字段类型一致
     *          3.2.3 缓存枚举中的所有字段(数值类型全按照Double缓存)
     *
     * @param clazz      类
     * @param fieldType  字段值
     * @param fieldName  字段名
     * @param annotation 注解
     * @throws DoCheckException
     */
    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckEnum annotation) throws DoCheckException {
        String value = annotation.value();
        String separator = annotation.separator();
        Class<? extends Enum> enumType = annotation.enumType();
        String enumField = annotation.enumField();

        isVerifyTrue(Emptys.isNotEmpty(value) ^ (enumType != DefaultEnum.class),"one and only one of `value` or `enumType` must exists", clazz, fieldType, fieldName, annotation);

        if(Emptys.isNotEmpty(value)){
            isVerifyTrue(Emptys.isNotEmpty(separator), "separator can't be null", clazz, fieldType, fieldName, annotation);

            NoneEmptySet<Object> split = fieldType == String.class ? getStringValues(value, separator) : getNumberValues(value, separator);
            isVerifyTrue(split.size() > 0, "no enum value", clazz, fieldType, fieldName, annotation);

            enumCache.put(annotation, split);
        } else {
            if(Emptys.isEmpty(enumField)){
                isVerifyTrue(fieldType == String.class, "field type must be string", clazz, fieldType, fieldName, annotation);
                enumCache.put(annotation, getEnums(enumType));
            }else {
                Optional<Class> enumFieldType = getEnumFieldType(enumType, enumField);
                isVerifyTrue(enumFieldType.isPresent(), "enumField is not exists", clazz, fieldType, fieldName, annotation);

                isVerifyTrue(enumFieldType.get() == fieldType, "enumField type is not match", clazz, fieldType, fieldName, annotation);

                enumCache.put(annotation, getEnumValues(enumType, enumField));
            }
        }
    }

    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckEnum annotation) throws DoCheckException {
        if (fieldValue != null) {
            NoneEmptySet<Object> objects = enumCache.get(annotation);
            Object fv = fieldValue instanceof Number ? ((Number) fieldValue).doubleValue() : fieldValue;
            isCheckTrue(objects.contains(fv), clazz, fieldName, fieldValue, annotation);
        }
    }

    private NoneEmptySet<Object> getStringValues(String value, String separator){
        return Arrays.stream(value.split(separator))
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .collect(NoneEmptySet.toSet());
    }

    private NoneEmptySet<Object> getNumberValues(String value, String separator){
        return Arrays.stream(value.split(separator))
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(Double::parseDouble)
                .collect(NoneEmptySet.toSet());
    }

    private NoneEmptySet<Object> getEnums(Class<? extends Enum> enumType){
        return Arrays.stream(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(NoneEmptySet.toSet());
    }
    
    private NoneEmptySet<Object> getEnumValues(Class<? extends Enum> enumType, String enumField){
        return Arrays.stream(enumType.getEnumConstants())
                .map(constant -> ReflectUtil.getFieldValue(constant, enumField))
                .map(object -> object instanceof Number ? ((Number) object).doubleValue() : object)
                .collect(NoneEmptySet.toSet());
    }

    private Optional<Class> getEnumFieldType(Class<? extends Enum> enumType, String enumField) {
        try {
            Enum[] constants = enumType.getEnumConstants();
            Field field = constants[0].getClass().getDeclaredField(enumField);
            return Optional.of(field.getType());
        }catch (NoSuchFieldException e){
            return Optional.empty();
        }
    }
}
