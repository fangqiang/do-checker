package cn.truthseeker.dochecker.mybatis.plugin;

import cn.truthseeker.dochecker.core.DoChecker;
import cn.truthseeker.dochecker.exception.DoCheckException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DoCheckerceptor implements Interceptor {

    private static String DELETE = "delete";
    private static String UPDATE = "update";
    private static String EMPTY_CHAR = " ";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object mappedStatement = args[0];
        Object params = args[1];

        // 对于delete的sql不校验字段
        SqlSource resource = ((MappedStatement) mappedStatement).getSqlSource();
        BoundSql boundSql = resource.getBoundSql(params);
        String sql = boundSql.getSql();
        if (isDelete(sql)) {
            return invocation.proceed();
        }

        Set<String> fields = isUpdate(sql) ?
                getUpdateFieldInSQL(sql) :
                boundSql.getParameterMappings().stream().map(ParameterMapping::getProperty).collect(Collectors.toSet());

        checkParamsRecursively(params, fields);

        //执行后面业务逻辑
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        try {
            DoChecker.getInstance().registerCheckableClassByPackage(properties.getProperty("registerPackage"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDelete(String sql) {
        return DELETE.equalsIgnoreCase(sql.split(EMPTY_CHAR)[0].trim());
    }

    private boolean isUpdate(String sql) {
        return UPDATE.equalsIgnoreCase(sql.split(EMPTY_CHAR)[0].trim());
    }

    /**
     * 获取update的字段
     *
     * @param sql
     * @return
     */
    private Pattern setPattern = Pattern.compile("set", Pattern.CASE_INSENSITIVE);
    private Pattern wherePattern = Pattern.compile("where", Pattern.CASE_INSENSITIVE);

    private Set<String> getUpdateFieldInSQL(String sql) {
        String replaceSql = setPattern.matcher(sql).replaceAll("SET");
        replaceSql = wherePattern.matcher(replaceSql).replaceAll("WHERE");
        String setSection = replaceSql.split("SET")[1].split("WHERE")[0];
        return Arrays.stream(setSection.split(","))
                .map(a -> a.split("=")[0].trim().replace("`", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    private void checkParamsRecursively(Object params, Set<String> fields) throws DoCheckException {
        if(params == null){
            return;
        }
        
        if(params instanceof Map){
            checkParamsRecursively(((Map) params).keySet(), fields);
            checkParamsRecursively(((Map) params).values(), fields);
        }else if(params instanceof Collection){
            for (Object o : ((Iterable) params)) {
                checkParamsRecursively(o, fields);
            }
        }else if(params.getClass().isArray()){
            for (Object o : ((Object[]) params)){
                checkParamsRecursively(o, fields);
            }
        }else{
            DoChecker.getInstance().checkOrException(params, fields);
        }
    }
}