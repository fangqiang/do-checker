# do-checker

## 功能说明
这是一个mybatis的插件。通过【注解】的方式，在写入数据库之前【自动】对【落库的字段】进行校验。校验失败则写入失败。



##### 关键字解释：

1. 【注解】：java中的Annotation
2. 【自动】：校验过程由mybatis插件自动完成
3. 【落库的字段】：insert 语句中所有字段，update语句中更新的字段

```mysql
// 自动校验`id`, `name`, `age` 字段
INSERT INTO TTT (`id`, `name`, `age`) VALUES (?, ?, ?);

// 自动校验`id`, `hobby`字段。（不会校验`name`，因为`name`没有落库）
UPDATE TTT SET `age`=?,`hobby`=? WHERE `name`=?;

// 不校验任何字段
DELETE FROM TTT WHERE `name`=?;
```



## 解决什么问题？

防止脏数据、语义不清的数据落库。

## 设计思想
1. 落入数据库的数据必须是语义明确的，要么不存在，如果存在则一定是有效的
2. 由数据库负责【全局】的数据校验：如主键，唯一键，外键
3. 由应用负责【每行数据】的校验：字段的校验
4. 该库只对字段校验【绝不修改字段】


PS：虽然数据库也能对字段做一些简单校验，但是很多业务校验是没法做的。如果字段校验规则既存在于数据库也存在程序中，则非常不利于维护。常见的情况是：前端有校验，后端有校验，数据库有校验。但仍然不能清楚的知道一个字段都做了什么校验，或者你要查看全流程代码才能知道。

## 使用方式

1. 引入mvn依赖
```xml
        <dependency>
            <groupId>cn.truthseeker</groupId>
            <artifactId>dochecker</artifactId>
            <version>1.0</version>
        </dependency>
```
2. 配置mybatis的插件
```xml
    <plugins>
        <plugin interceptor="cn.truthseeker.dochecker.mybatis.plugin.DoCheckerceptor">
            <property name="registerPackage" value="你所有DO所在的包名" />  <!--非常重要，只对该包下面的对象中的字段进行校验--> 
        </plugin>
    </plugins>
```
3. 为需要校验的类，字段添加注解
~~~java
```
public class TestDO {                           // 支持继承其他DO

    @CheckNotNull                               // 如果为null，则抛出异常，默认允许为null
    private Long id;
 
    @CheckExpress("18<age && age<60")           // 校验是否满足表达式（java语法的表达式，`age`为变量名（要跟字段名保持一致），返回值必须是boolean类型），返回false则抛出异常
    private Integer age;                        
 
    @CheckEnum("basketball,football")           // 校验枚举类型
    // @CheckEnum(enumType = HobbyEnum.class)   // 校验枚举类型，按java枚举类校验
    private String hobby;
     
    @CheckStringFormat(FormatType.VALID_JSON)   // 如果是空字符串或者空json则抛出异常（如："", " ", {},[] 均不合法）
    private String name;
    
    @CheckStringFormat({FormatType.ALPHA, FormatType.CAMEL}) // 必须全是字母，且是驼峰格式
    private String type;
 
    @CheckByMethod("checkPassword")             // 调用checkPassword函数校验（函数返回值必须boolean类型），返回false则抛出异常
    private String password;
 
    public boolean checkPassword() {            // 用于校验password字段的函数
        return password.equals(age);
    }
    
    ………… 省略getXXX()  省略setXXX()…………

```
~~~

## 使用限制
1. 字段定义不能使用基本类型，必须包装成引用类型（如：int -> Integer）
   1. 因为基本类型会自动填充默认值（如：int 类型默认为0）。而默认值通常是模棱两可的，你并不知道这是正确值，还是默认值。
2. 确保你要校验的DO类放在registerPackage下，否则不进行校验
3. 每个注解只能配置在其允许的类型上，参见每个校验注解上的Scope信息（示例如下）
~~~java
```
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Scope({String.class,Number.class})     // 说明CheckEnum只能用于String, Number字段
public @interface CheckEnum {
    String separator() default ",";

    String value();
}

```
~~~
## 高级内容

### 自定义注解  

TODO
