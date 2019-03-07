package cn.truthseeker.dochecker;


import cn.truthseeker.dochecker.annotations.CheckNotNull;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/5
 */
public class YyyDO {


    @CheckNotNull
    private Integer age=1;

    public Integer getAge() {
        return age+1;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
