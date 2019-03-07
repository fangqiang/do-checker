package cn.truthseeker.dochecker.core;

import cn.truthseeker.dochecker.annotations.CheckNotNull;
import cn.truthseeker.dochecker.exception.DoCheckException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/7
 */
public class DoCheckerTest {

    DoChecker doChecker = DoChecker.getInstance();

    @BeforeClass
    public static void beforeClass() throws DoCheckException {
        DoChecker.getInstance().cleanForTest();
    }

    @Before
    public void before() throws DoCheckException {
        doChecker.cleanForTest();
    }

    @Test
    public void registerCheckableClass() throws DoCheckException {
        doChecker.registerCheckableClass(AnnoClass.class);
        Assert.assertTrue(doChecker.getMetaCache().size() == 0);

        doChecker.registerCheckableClass(TwoAnnoClass.class);
        Assert.assertTrue(doChecker.getMetaCache().size() == 1);
    }

    @Test
    public void checkOrException() throws DoCheckException {
        TwoAnnoClass instance = new TwoAnnoClass();
        instance.a = 1;

        doChecker.registerCheckableClass(TwoAnnoClass.class);
        doChecker.registerCheckableClass(NoAnnoClass.class);

        doChecker.checkOrException(instance);

        doChecker.checkOrException(1);
        doChecker.checkOrException(1, Collections.EMPTY_SET);
        doChecker.checkOrException(1, new HashSet<>(Arrays.asList("aaa")));
        doChecker.checkOrException(new NoAnnoClass(), new HashSet<>(Arrays.asList("aaa")));

    }

    @Test
    public void registerCheckableClassByPackage() throws DoCheckException {
        doChecker.registerCheckableClassByPackage("cn.truthseeker.dochecker.util");
    }


    static class AnnoClass {
    }

    @NotThreadSafe
    static class TwoAnnoClass {
        @CheckNotNull
        Integer a = 5;
    }

    static class NoAnnoClass {
        int a;
        Integer b = 2;
    }
}