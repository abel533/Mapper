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

import com.github.abel533.entity.MySqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 针对Mysql的方法
 *
 * Created by liuzh on 2015/1/16.
 */
public interface MysqlMapper extends EntityMapper {

    /**
     * 分页查询
     * <p/>
     * <br> 可以配合CommonMapper中的selectCount查询总数
     *
     * @param record
     * @param offset
     * @param limit
     * @param <T>
     * @return
     */
    @SelectProvider(type = MySqlProvider.class, method = "selectPage")
    <T> List<Map> selectPage(@Param("record") T record, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 插入数据库，主键为id自增，可以回写id
     *
     * @param record
     * @param <T>
     * @return
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = MySqlProvider.class, method = "insertIdentity")
    <T> int insertIdentity(@Param("record") T record);

    /**
     * 批量插入List
     *
     * @param records
     * @param <T>
     * @return
     */
    @InsertProvider(type = MySqlProvider.class, method = "insertList")
    <T> int insertList(@Param("records") List<T> records);

    /**
     * 通过id数组批量删除
     *
     * @param idName
     * @param ids
     * @param <T>
     * @return
     */
    @DeleteProvider(type = MySqlProvider.class, method = "deleteList")
    <T> int deleteList(@Param("idName") String idName, @Param("ids") int[] ids);
}