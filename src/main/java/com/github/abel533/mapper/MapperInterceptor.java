/*
	The MIT License (MIT)

	Copyright (c) 2014 abel533@gmail.com

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

package com.github.abel533.mapper;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * 通用Mapper拦截器
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MapperInterceptor implements Interceptor {

    private final MapperHelper mapperHelper = new MapperHelper();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        String msId = ms.getId();
        //不需要拦截的方法直接返回
        if (mapperHelper.isMapperMethod(msId)) {
            //第一次经过处理后，就不会是ProviderSqlSource了，一开始高并发时可能会执行多次，但不影响。以后就不会在执行了
            if (ms.getSqlSource() instanceof ProviderSqlSource) {
                mapperHelper.setSqlSource(ms);
            }
        }
        return invocation.proceed();
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
        String UUID = properties.getProperty("UUID");
        if (UUID != null && UUID.length() > 0) {
            mapperHelper.setUUID(UUID);
        }
        String IDENTITY = properties.getProperty("IDENTITY");
        if (IDENTITY != null && IDENTITY.length() > 0) {
            mapperHelper.setIDENTITY(IDENTITY);
        }
        String seqFormat = properties.getProperty("seqFormat");
        if (seqFormat != null && seqFormat.length() > 0) {
            mapperHelper.setSeqFormat(seqFormat);
        }
        String ORDER = properties.getProperty("ORDER");
        if (ORDER != null && ORDER.length() > 0) {
            mapperHelper.setBEFORE(ORDER);
        }
        int mapperCount = 0;
        String mapper = properties.getProperty("mappers");
        if (mapper != null && mapper.length() > 0) {
            String[] mappers = mapper.split(",");
            for (String mapperClass : mappers) {
                if (mapperClass.length() > 0) {
                    mapperHelper.registerMapper(mapperClass);
                    mapperCount++;
                }
            }
        }
        if (mapperCount == 0) {
            throw new RuntimeException("通用Mapper没有配置任何有效的通用接口!");
        }
    }
}
