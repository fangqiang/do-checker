package cn.truthseeker.dochecker.core.bean;

import cn.truthseeker.dochecker.annotations.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class VerifyDOFactory {
    public static class ErrorMethod {
        @CheckByMethod("check")
        private String a;

        public boolean check() {
            return false;
        }
    }

    public static class ErrorContainerEmpty {
        @CheckContainerNotEmpty
        private Map a = new HashMap();
    }

    public static class ErrorEnum {
        @CheckEnum("a,b")
        private String a="c";
    }

    public static class ErrorEnum1 {
        @CheckEnum("1,2")
        private Integer a=3;
    }

    public static class ErrorEnum2 {
        @CheckEnum(enumType = EnumAAA.class, enumField = "score")
        private Integer a=3;
    }

    public static class ErrorEnum3 {
        @CheckEnum(enumType = EnumAAA.class, enumField = "name")
        private String a="a";
    }

    public static class ErrorExpress {
        @CheckExpress("a.length() > 1")
        private String a="a";
    }


    public static class ErrorNotNull {
        @CheckNotNull
        private Integer a;
    }

    public static class ErrorStringFormat {
        @CheckStringFormat({FormatType.VALID_JSON})
        private String a = "";
    }
}
