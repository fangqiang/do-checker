package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.container.SimpleCache;
import cn.truthseeker.dochecker.annotations.CheckExpress;
import cn.truthseeker.dochecker.exception.DoCheckException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class ExpressHandler implements CheckHandler<CheckExpress> {

    SimpleCache<String, CompiledExpression> cache = new SimpleCache<>();

    /**
     * 1. 表达式不能为空
     * 2. 表达式语法正确，能被正确编译
     *
     * @param clazz      类
     * @param fieldType  字段值
     * @param fieldName  字段名
     * @param annotation 注解
     * @throws DoCheckException
     */
    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckExpress annotation) throws DoCheckException {
        String express = annotation.value();

        isVerifyTrue(StringUtils.isNotBlank(express),"empty content is not allowed", clazz, fieldType, fieldName, annotation);

        try {
            cache.put(express, new ExpressionCompiler(express).compile());
        } catch (Throwable e) {
            throw new DoCheckException(buildInitErrorMessage("annotation compile express error", clazz, fieldType, fieldName, annotation));
        }
    }

    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckExpress annotation) throws DoCheckException {
        if (fieldValue != null) {
            String express = annotation.value();
            CompiledExpression compile = cache.get(express);
            boolean ok = (boolean) MVEL.executeExpression(compile, ImmutableMap.of(fieldName, fieldValue));
            isCheckTrue(ok, clazz, fieldName, fieldValue, annotation);
        }
    }
}
