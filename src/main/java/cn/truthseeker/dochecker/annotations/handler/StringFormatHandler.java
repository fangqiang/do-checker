package cn.truthseeker.dochecker.annotations.handler;

import cn.truthseeker.dochecker.annotations.FormatType;
import cn.truthseeker.dochecker.annotations.CheckStringFormat;
import cn.truthseeker.dochecker.exception.DoCheckException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/6
 */
public class StringFormatHandler implements CheckHandler<CheckStringFormat> {

    @Override
    public void verifyUsage2(Class clazz, Class fieldType, String fieldName, CheckStringFormat annotation) throws DoCheckException { }

    @Override
    public void verifyValue(Object instance, Class clazz, Class fieldType, String fieldName, Object fieldValue, CheckStringFormat annotation) throws DoCheckException {
        if (fieldValue != null) {
            String val = fieldValue.toString();

            FormatType[] formatType = annotation.value();
            for (FormatType style : formatType) {
                boolean ok = false;
                switch (style) {
                    case NOT_EMPTY:
                        ok = StringUtils.isNotEmpty(val);
                        break;
                    case VALID_JSON:
                        ok = isValidJSON(val);
                        break;
                    case CAMEL:
                        ok = !val.contains("_");
                        break;
                    case UNDERSCORE:
                        ok = val.contains("_");
                        break;
                    case ALPHA_NUMERIC:
                        ok = StringUtils.isAlphanumeric(val);
                        break;
                    case NUMERIC:
                        ok = NumberUtils.isCreatable(val);
                        break;
                    case ALPHA:
                        ok = StringUtils.isAlpha(val);
                        break;
                    default:
                        break;
                }

                isCheckTrue(ok, clazz, fieldName, fieldValue, annotation);
            }
        }
    }

    private boolean isValidJSON(String json){
        try {
            Object object = JSON.parse(json);
            return object instanceof JSONObject ?
                    !((JSONObject) object).isEmpty() :
                    !((JSONArray) object).isEmpty();
        } catch (Throwable e) {
            return false;
        }
    }
}
