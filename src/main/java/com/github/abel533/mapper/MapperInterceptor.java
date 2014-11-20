package com.github.abel533.mapper;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Created by liuzh on 2014/11/19.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MapperInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        Object parameterObject = objects[1];
        String msId = ms.getId();
        //不需要拦截的方法直接返回
        if (!MapperHelper.isMapperMethod(msId)) {
            return invocation.proceed();
        }
        String methodName = msId.substring(msId.lastIndexOf(".") + 1);
        ResultMap resultMap = ms.getResultMaps().get(0);
        if (resultMap.getType().equals(Object.class)) {
            Class T = MapperHelper.getSelectReturnType(ms);
            MetaObject metaObject = MapperHelper.forObject(resultMap);
            switch (ms.getSqlCommandType()) {
                case SELECT:
                    metaObject.setValue("type", T);
                    MapperHelper.selectSqlSource(ms);
                    break;
                case INSERT:
                    metaObject.setValue("type", int.class);
                    MapperHelper.insertSqlSource(ms);
                    break;
                case UPDATE:
                    metaObject.setValue("type", int.class);
                    MapperHelper.updateSqlSource(ms);
                    break;
                case DELETE:
                    metaObject.setValue("type", int.class);
                    MapperHelper.deleteSqlSource(ms);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        Object result = invocation.proceed();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
