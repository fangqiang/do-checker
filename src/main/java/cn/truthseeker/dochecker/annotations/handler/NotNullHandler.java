package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.dochecker.annotations.CheckNotNull;
import cn.truthseeker.dochecker.exception.DoCheckException;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class NotNullHandler implements CheckHandler<CheckNotNull> {

    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckNotNull annotation) {
    }


    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckNotNull annotation) throws DoCheckException {
        if (fieldValue == null) {
            throw new DoCheckException(buildCheckErrorMessage("annotation check failed", clazz, fieldName, fieldValue, annotation));
        }
    }
}
