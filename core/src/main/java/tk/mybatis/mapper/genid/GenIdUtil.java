/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
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

package tk.mybatis.mapper.genid;

import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuzh
 * @since 2018-04-22
 */
public class GenIdUtil {

    public static final Map<Class<? extends GenId>, GenId> CACHE = new ConcurrentHashMap<Class<? extends GenId>, GenId>();

    public static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 生成 Id
     *
     * @param target
     * @param property
     * @param genClass
     * @param table
     * @param column
     * @throws MapperException
     */
    public static void genId(Object target, String property, Class<? extends GenId> genClass, String table, String column) throws MapperException {
        try {
            GenId genId;
            if (CACHE.containsKey(genClass)) {
                genId = CACHE.get(genClass);
            } else {
                LOCK.lock();
                try {
                    if (!CACHE.containsKey(genClass)) {
                        CACHE.put(genClass, genClass.newInstance());
                    }
                    genId = CACHE.get(genClass);
                } finally {
                    LOCK.unlock();
                }
            }
            Object id = genId.genId(table, column);
            //赋值
            MetaObject metaObject = MetaObjectUtil.forObject(target);
            metaObject.setValue(property, id);
        } catch (Exception e) {
            throw new MapperException("生成 ID 失败!", e);
        }
    }

}
