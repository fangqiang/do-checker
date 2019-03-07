package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.dochecker.annotations.CheckContainerNotEmpty;
import cn.truthseeker.dochecker.exception.DoCheckException;

import java.util.Collection;
import java.util.Map;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class ContainerNotEmptyHandler implements CheckHandler<CheckContainerNotEmpty> {

    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckContainerNotEmpty annotation) throws DoCheckException { }

    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckContainerNotEmpty annotation) throws DoCheckException {
        if (fieldValue != null) {
            isCheckTrue(isNotEmptyMap(fieldValue) || isNotEmptyCollection(fieldValue), clazz, fieldName, fieldValue, annotation);
        }
    }

    private boolean isNotEmptyMap(Object object){
        return object instanceof Map && !((Map)object).isEmpty();
    }

    private boolean isNotEmptyCollection(Object object){
        return object instanceof Collection && !((Collection)object).isEmpty();
    }
}
