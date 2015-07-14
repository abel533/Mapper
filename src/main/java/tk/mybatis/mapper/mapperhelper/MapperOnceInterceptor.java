/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.mapperhelper;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * 通用Mapper拦截器 - 该拦截器执行后会自动卸载，以后不会再进入该方法
 *
 * @author liuzh
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MapperOnceInterceptor implements Interceptor {

    private final MapperHelper mapperHelper = new MapperHelper();

    private Boolean hasRan = false;

    /**
     * 处理所有通用Mapper接口
     *
     * @param invocation
     */
    public synchronized void processInvocation(Invocation invocation) {
        if (!hasRan) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];
            mapperHelper.processConfiguration(ms.getConfiguration());
            //移除当前拦截器
            InterceptorChain interceptorChain = new InterceptorChain();
            List<Interceptor> interceptorList = ms.getConfiguration().getInterceptors();
            for (Interceptor interceptor : interceptorList) {
                if (interceptor != this) {
                    interceptorChain.addInterceptor(interceptor);
                }
            }
            MetaObject metaObject = SystemMetaObject.forObject(ms.getConfiguration());
            metaObject.setValue("interceptorChain", interceptorChain);
            hasRan = true;
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //同步方法，防止多次执行
        processInvocation(invocation);
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
        mapperHelper.setProperties(properties);
    }
}
