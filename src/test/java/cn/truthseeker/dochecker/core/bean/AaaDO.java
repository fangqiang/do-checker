package cn.truthseeker.dochecker.core.bean;

import cn.truthseeker.dochecker.annotations.*;

import java.util.Map;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class AaaDO extends BbbDO {


    @CheckNotNull
    public String notNull;

    @CheckNotNull
    @CheckEnum("A,B")
    public String enumAB;

    @CheckNotNull
    @CheckEnum("1.0,2")
    public Integer enum12;

    @CheckNotNull
    @CheckEnum(enumType = EnumAAA.class, enumField = "name")
    public String enumName;

    @CheckNotNull
    @CheckEnum(enumType = EnumAAA.class, enumField = "score")
    public Double enumScore;

    @CheckNotNull
    @CheckEnum(enumType = EnumAAA.class)
    public String enumS;

    @CheckNotNull
    @CheckStringFormat(FormatType.ALPHA)
    public String formatAlpha;

    @CheckNotNull
    @CheckStringFormat(FormatType.NUMERIC)
    public String formatNumeric;

    @CheckNotNull
    @CheckStringFormat(FormatType.NOT_EMPTY)
    public String standString;

    @CheckNotNull
    @CheckStringFormat(FormatType.VALID_JSON)
    public String validJson;

    @CheckNotNull
    @CheckStringFormat(FormatType.ALPHA_NUMERIC)
    public String alphaNumber;

    @CheckNotNull
    @CheckStringFormat({FormatType.NOT_EMPTY,FormatType.CAMEL})
    public String camel;

    @CheckNotNull
    @CheckStringFormat(FormatType.UNDERSCORE)
    public String underscore;

    @CheckNotNull
    @CheckExpress("express.length() > 3")
    public String express;

    @CheckNotNull
    @CheckByMethod("checkMethod")
    private String checkByMethod;

    public boolean checkMethod() {
        return Boolean.parseBoolean(checkByMethod);
    }

    public void setCheckByMethod(String checkByMethod) {
        this.checkByMethod = checkByMethod;
    }

    @CheckContainerNotEmpty
    public Map map;
}
