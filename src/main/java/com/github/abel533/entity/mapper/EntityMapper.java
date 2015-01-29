/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
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

package com.github.abel533.entity.mapper;

import com.github.abel533.entity.EntityProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 这个仍然是接口类，不需要被继承，可以直接用
 *
 * @author liuzh
 */
public interface EntityMapper {
    /**
     * 根据参数进行查询,record可以是Class<?>类型
     * <br>查询条件为属性String类型不为空，其他类型!=null时
     * <br>where property = ? and property2 = ? 条件
     *
     * @param record
     * @param <T>
     * @return
     */
	@SelectProvider(type = EntityProvider.class, method = "select")
    <T> List<Map<String,Object>> select(@Param("record") T record);

    /**
     * 根据参数进行查询总数,record可以是Class<?>类型
     * <br>查询条件为属性String类型不为空，其他类型!=null时
     * <br>where property = ? and property2 = ? 条件
     *
     * @param record
     * @param <T>
     * @return
     */
    @SelectProvider(type = EntityProvider.class, method = "count")
    <T> int count(@Param("record") T record);

    /**
     * 根据主键查询结果，主键不能为null或空
     *
     * @param entityClass
     * @param key
     * @param <T>
     * @return
     */
    @SelectProvider(type = EntityProvider.class, method = "selectByPrimaryKey")
    <T> Map<String,Object> selectByPrimaryKey(@Param("entityClass") Class<T> entityClass, @Param("key") Object key);

    /**
     * 插入数据库，主键字段没有值的时候不会出现在sql中
     * <br>如果是自增主键，会自动获取值
     * <br>如果是自增主键，并且该主键属性有值，会使用主键的属性值，不会使用自增
     *
     * @param record
     * @param <T>
     * @return
     */
    @InsertProvider(type = EntityProvider.class, method = "insert")
    <T> int insert(@Param("record") T record);

    /**
     * 插入非空字段，其他和上面方法类似
     *
     * @param record
     * @param <T>
     * @return
     */
    @InsertProvider(type = EntityProvider.class, method = "insertSelective")
    <T> int insertSelective(@Param("record") T record);

    /**
     * 根据条件进行删除，条件不能为空，并且必须有至少一个条件才能删除
     * <br>该方法不能直接删除全部数据
     *
     * @param record
     * @param <T>
     * @return
     */
    @DeleteProvider(type = EntityProvider.class, method = "delete")
    <T> int delete(@Param("record") T record);

    /**
     * 根据主键进行删除，主键不能为null或空
     *
     * @param entityClass
     * @param key
     * @param <T>
     * @return
     */
    @DeleteProvider(type = EntityProvider.class, method = "deleteByPrimaryKey")
    <T> int deleteByPrimaryKey(@Param("entityClass") Class<T> entityClass, @Param("key") Object key);

    /**
     * 根据主键更新全部字段，空字段也会更新数据库
     *
     * @param record
     * @param <T>
     * @return
     */
    @UpdateProvider(type = EntityProvider.class, method = "updateByPrimaryKey")
    <T> int updateByPrimaryKey(@Param("record") T record);

    /**
     * 根据主键更新非空属性字段，不能给数据库数据设置null或空
     *
     * @param record
     * @param <T>
     * @return
     */
    @UpdateProvider(type = EntityProvider.class, method = "updateByPrimaryKeySelective")
    <T> int updateByPrimaryKeySelective(@Param("record") T record);
}