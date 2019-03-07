package cn.truthseeker.dochecker;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/28
 */
public enum  EnumA {
    AAA("A1"),BBB("B1");

    public String getS() {
        return s;
    }

    private String s;

    EnumA(String b) {
        s=b;
    }
}
