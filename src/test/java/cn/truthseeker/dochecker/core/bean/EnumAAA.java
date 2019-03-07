package cn.truthseeker.dochecker.core.bean;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/28
 */
public enum EnumAAA {
    AAA("A1",1.000),BBB("B1",2);

    private String name;
    private Double score;

    EnumAAA(String b, double i) {
        name=b;
        score=i;
    }
}
