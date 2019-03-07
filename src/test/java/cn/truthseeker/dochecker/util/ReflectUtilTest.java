package cn.truthseeker.dochecker.util;

import cn.truthseeker.dochecker.annotations.CheckExpress;
import cn.truthseeker.dochecker.annotations.CheckNotNull;
import cn.truthseeker.dochecker.annotations.DefaultEnum;
import com.google.common.util.concurrent.AtomicDouble;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class ReflectUtilTest {
    @Test
    public void testDefaultEnum() {
        try {
            DefaultEnum.valueOf("");
        }catch (Exception e){}
    }

    @Test
    public void getAllAnnotation() throws Exception {
        Assert.assertTrue(ReflectUtil.getAllFieldsIncludeInherited(EmptyClazz.class).size() == 0);
        Assert.assertTrue(ReflectUtil.getAllFieldsIncludeInherited(new EmptyClazz()).size() == 0);

        Assert.assertTrue(ReflectUtil.getAllFieldsIncludeInherited(ClearField.class).size() == 1);
        Assert.assertTrue(ReflectUtil.getAllFieldsIncludeInherited(new ClearField()).size() == 1);

        Field a = NoAnnoClass.class.getDeclaredField("a");
        Field b = NoAnnoClass.class.getDeclaredField("b");
        Assert.assertTrue(ReflectUtil.getAllAnnotation(a).length == 0);
        Assert.assertTrue(ReflectUtil.getAllAnnotation(b).length == 2);

        Assert.assertTrue(ReflectUtil.isPrimaryType(1));
        Assert.assertTrue(ReflectUtil.getFieldValue(new ClearField(), ClearField.class.getDeclaredField("a")).equals(0));
    }

    @Test
    public void getAllClass() {
        Assert.assertTrue(ReflectUtil.getAllClass("cn.truthseeker.dochecker").size() >0);
    }

    @Test
    public void isStringType() {
        Assert.assertTrue(ReflectUtil.isStringType(String.class));
    }

    @Test
    public void isNumericType() {
        Assert.assertTrue(ReflectUtil.isNumericType(new Integer(1).getClass()));
        Assert.assertTrue(ReflectUtil.isNumericType(Integer.class));
        Assert.assertTrue(ReflectUtil.isNumericType(Long.class));
        Assert.assertTrue(ReflectUtil.isNumericType(Float.class));
        Assert.assertTrue(ReflectUtil.isNumericType(Double.class));
        Assert.assertTrue(ReflectUtil.isNumericType(Byte.class));
        Assert.assertTrue(ReflectUtil.isNumericType(Short.class));

        Assert.assertTrue(ReflectUtil.isNumericType(AtomicInteger.class));
        Assert.assertTrue(ReflectUtil.isNumericType(AtomicLong.class));
        Assert.assertTrue(ReflectUtil.isNumericType(AtomicDouble.class));
    }

    @Test
    public void isContainerType() {
        Assert.assertTrue(ReflectUtil.isCollectionType(Collection.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(List.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(ArrayList.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(LinkedList.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(Set.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(HashSet.class));
        Assert.assertTrue(ReflectUtil.isCollectionType(TreeSet.class));

        Assert.assertTrue(ReflectUtil.isMapType(Map.class));
        Assert.assertTrue(ReflectUtil.isMapType(HashMap.class));
        Assert.assertTrue(ReflectUtil.isMapType(TreeMap.class));

        Assert.assertFalse(ReflectUtil.isCollectionType(Object.class));
        Assert.assertFalse(ReflectUtil.isCollectionType(String.class));
        Assert.assertFalse(ReflectUtil.isCollectionType(Long.class));
        Assert.assertFalse(ReflectUtil.isCollectionType(int.class));
    }

    static class EmptyClazz {
    }

    static class ClearField {
        int a;
    }

    static class NoAnnoClass {
        int a;
        @CheckNotNull
        @CheckExpress("true")
        Integer b;
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface TestAnno {
    }
}