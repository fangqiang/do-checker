package cn.truthseeker.dochecker.core.bean;

import cn.truthseeker.dochecker.annotations.CheckNotNull;
import cn.truthseeker.dochecker.annotations.CheckExpress;

/**
 * @Description:
 * @author: qiang.fang
 * @email: lowping@163.com
 * @date: Created by on 19/3/10
 */
public class BbbDO {

    @CheckNotNull
    @CheckExpress("18<numberExpress && numberExpress<60")
    private Integer numberExpress;

    public Integer getNumberExpress() {
        return numberExpress;
    }

    public void setNumberExpress(Integer numberExpress) {
        this.numberExpress = numberExpress;
    }
}
