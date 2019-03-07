package cn.truthseeker.dochecker.core.bean;

import cn.truthseeker.dochecker.annotations.*;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class DOFactory {
    public static class ErrorMethod {
        @CheckByMethod("aaa")
        private String a;

        public boolean check() {
            return true;
        }
    }

    public static class ErrorMethod1 {
        @CheckByMethod("check")
        private String a;

        public int check() {
            return 1;
        }
    }

    public static class ErrorContainerEmpty {
        @CheckContainerNotEmpty
        private Integer a;
    }

    public static class ErrorEnum {
        @CheckEnum("a,b")
        private int a;
    }

    public static class ErrorEnum0 {
        @CheckEnum(value = "a,b",separator = "")
        private String a;
    }

    public static class ErrorEnum1 {
        @CheckEnum("")
        private String a;
    }

    public static class ErrorEnum2 {
        @CheckEnum(",,")
        private String a;
    }

    public static class ErrorEnum3 {
        @CheckEnum(enumType = EnumAAA.class)
        private Double a;
    }

    public static class ErrorEnum4 {
        @CheckEnum(enumType = EnumAAA.class, enumField = "score")
        private String a;
    }

    public static class ErrorEnum5 {
        @CheckEnum(enumType = EnumAAA.class, enumField = "null")
        private String a;
    }

    public static class ErrorExpress {
        @CheckExpress("a.length( > 1")
        private String a="";
    }

    public static class ErrorExpress1 {
        @CheckExpress("")
        private String a="";
    }

    public static class ErrorNotNull {
        @CheckNotNull
        private int a = 1;
    }

    public static class ErrorStringFormat {
        @CheckStringFormat({FormatType.ALPHA})
        private int a;
    }
}
