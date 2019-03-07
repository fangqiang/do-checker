package cn.truthseeker.dochecker;

import cn.truthseeker.dochecker.annotations.Scope;
import cn.truthseeker.dochecker.core.DoChecker;
import cn.truthseeker.dochecker.exception.DoCheckException;
import cn.truthseeker.dochecker.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/8
 */
public class App {
    public static void main(String[] args) throws DoCheckException, Exception {
//        EnumA a = EnumA.valueOf("AAA");
//        System.out.println(a.toString()+"===");
//        System.out.println(Enum.class.isAssignableFrom(EnumA.class));
//        Class<? super EnumA> superclass = EnumA.class.getSuperclass();
//        EnumA[] enumConstants = EnumA.class.getEnumConstants();
//        for (EnumA enumConstant : enumConstants) {
//            Field declaredFields = enumConstant.getClass().getDeclaredField("s");
//            Object fieldValue = ReflectUtil.getFieldValue(enumConstant, declaredFields);
//            System.out.println(enumConstant);
//        }

        Double i = new Double(1.0);
        Integer j = new Integer(1);
        System.out.println(i.equals(j));
        XxxDO testDO = new XxxDO();
        testDO.setId(1L);
        testDO.setAge(30);
        testDO.setName("BOB");
        testDO.setMessage("abcd");
        testDO.hobby="eat";
        testDO.hobby1=2;
        testDO.hobby2="A1";

        DoChecker.getInstance().registerAndCheck(testDO);
    }

}
