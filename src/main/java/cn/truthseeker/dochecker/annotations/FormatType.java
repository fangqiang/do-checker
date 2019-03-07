package cn.truthseeker.dochecker.annotations;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/5
 */
public enum FormatType {
    /**
     * 不能为null或只有空字符（" ","\r","\t","\n"）等等
     */
    NOT_EMPTY,
    /**
     * 有效json，不允许 ""," ","[]","{}" 等无意义的存在，【存在即合理】
     */
    VALID_JSON,
    /**
     * 是否只包含数据
     */
    NUMERIC,
    /**
     * 全是字母（不包含特殊字符）
     */
    ALPHA,
    /**
     * 只包含字母和数字
     */
    ALPHA_NUMERIC,
    /**
     * 驼峰格式
     */
    CAMEL,
    /**
     * 下划线格式
     */
    UNDERSCORE;
}
