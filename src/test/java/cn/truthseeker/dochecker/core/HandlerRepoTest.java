package cn.truthseeker.dochecker.core;

import cn.truthseeker.container.safe.Maps;
import cn.truthseeker.dochecker.core.bean.AaaDO;
import cn.truthseeker.dochecker.core.bean.DOFactory;
import cn.truthseeker.dochecker.core.bean.VerifyDOFactory;
import cn.truthseeker.dochecker.exception.DoCheckException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/7
 */
public class HandlerRepoTest {

    AaaDO aaaDO = new AaaDO();


    @BeforeClass
    public static void beforeClass() throws DoCheckException {
        DoChecker.getInstance().cleanForTest();
        DoChecker.getInstance().registerCheckableClass(AaaDO.class);
    }

    @Before
    public void before() {
        aaaDO.enum12=1;
        aaaDO.enumAB="A";
        aaaDO.express="abcd";
        aaaDO.formatAlpha="abc";
        aaaDO.formatNumeric="-123";
        aaaDO.notNull="123";
        aaaDO.standString="123";
        aaaDO.validJson="{1:1}";
        aaaDO.setCheckByMethod("true");
        aaaDO.setNumberExpress(30);
        aaaDO.alphaNumber="12ab";
        aaaDO.camel="aaabBbCCc";
        aaaDO.underscore="aaabB_bCCc";
        aaaDO.map = Maps.of(1,1);
        aaaDO.enumName="A1";
        aaaDO.enumScore=1.0;
        aaaDO.enumS="AAA";
    }

    @Test
    public void testOk() throws DoCheckException {
        DoChecker.getInstance().registerCheckableClass(AaaDO.class);
        DoChecker.getInstance().checkOrException(aaaDO);
        DoChecker.getInstance().registerAndCheck(aaaDO);
    }

    @Test
    public void testUsage() {
        Assert.assertTrue(isException(DOFactory.ErrorMethod.class) + isException(DOFactory.ErrorMethod1.class) == 2);
        Assert.assertTrue(isException(DOFactory.ErrorContainerEmpty.class) == 1);
        Assert.assertTrue(isException(DOFactory.ErrorEnum.class)+isException(DOFactory.ErrorEnum0.class)+isException(DOFactory.ErrorEnum1.class)+isException(DOFactory.ErrorEnum2.class)+isException(DOFactory.ErrorEnum3.class)+isException(DOFactory.ErrorEnum4.class)+isException(DOFactory.ErrorEnum5.class) == 7);
        Assert.assertTrue(isException(DOFactory.ErrorExpress.class)+isException(DOFactory.ErrorExpress1.class) == 2);
        Assert.assertTrue(isException(DOFactory.ErrorNotNull.class) == 1);
        Assert.assertTrue(isException(DOFactory.ErrorStringFormat.class) == 1);
    }

    @Test
    public void testVerify() {
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorMethod()) == 1);
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorContainerEmpty()) == 1);
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorEnum())+isException(new VerifyDOFactory.ErrorEnum1())+isException(new VerifyDOFactory.ErrorEnum2())+isException(new VerifyDOFactory.ErrorEnum3())==4);
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorExpress()) == 1);
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorNotNull()) == 1);
        Assert.assertTrue(isException(new VerifyDOFactory.ErrorStringFormat()) == 1);
    }

    private int isException(Class clazz) {
        try {
            DoChecker.getInstance().registerCheckableClass(clazz);
            return 0;
        } catch (DoCheckException e) {
            return 1;
        }
    }

    private int isException(Object object) {
        try {
            DoChecker.getInstance().registerAndCheck(object);
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}