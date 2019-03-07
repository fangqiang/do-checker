package cn.truthseeker.dochecker;


import cn.truthseeker.dochecker.annotations.*;

import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @author: fang
 * @date: Created by on 19/3/5
 */
public class XxxDO extends YyyDO {

    @CheckNotNull
    private Long id;

    @CheckNotNull
    @CheckStringFormat({FormatType.NOT_EMPTY, FormatType.ALPHA})
    private String name;

    @CheckNotNull
    @CheckExpress("message.length() > 3")
    private String message;

    @CheckNotNull
    @CheckEnum("eat,sleep")
    public String hobby;

    @CheckNotNull
    @CheckEnum("1, 2.0")
    public Integer hobby1;

    @CheckNotNull
    @CheckEnum(enumType = EnumA.class, enumField = "s")
    public String hobby2;

    @CheckByMethod("checkPassword")
    private String password;

    @CheckContainerNotEmpty
    private Map map;

    @CheckContainerNotEmpty
    private Set set;

    public boolean checkPassword() {
        return true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
