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

import com.github.abel533.entity.ExampleProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

/**
 * Example查询，Example需要是Mybatis生成的Example类
 *
 * @author liuzh
 */
public interface ExampleMapper {

    /**
     * 通过Example类来查询count
     *
     * @param entityClass
     * @param example
     * @param <T>
     * @return
     */
    @SelectProvider(type = ExampleProvider.class, method = "countByExample")
    <T> int countByExample(@Param("entityClass") Class<T> entityClass, @Param("example") Object example);

    /**
     * 通过Example删除
     *
     * @param entityClass
     * @param example
     * @param <T>
     * @return
     */
    @DeleteProvider(type = ExampleProvider.class, method = "deleteByExample")
    <T> int deleteByExample(@Param("entityClass") Class<T> entityClass, @Param("example") Object example);

    /**
     * 通过Example来查询
     *
     * @param entityClass
     * @param example
     * @param <T>
     * @return
     */
    @SelectProvider(type = ExampleProvider.class, method = "selectByExample")
    <T> List<Map<String,Object>> selectByExample(@Param("entityClass") Class<T> entityClass, @Param("example") Object example);

    /**
     * 通过Example进行更新非空字段
     *
     * @param record
     * @param example
     * @param <T>
     * @return
     */
    @UpdateProvider(type = ExampleProvider.class, method = "updateByExampleSelective")
    <T> int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);

    /**
     * 通过Example进行更新全部字段
     *
     * @param record
     * @param example
     * @param <T>
     * @return
     */
    @UpdateProvider(type = ExampleProvider.class, method = "updateByExample")
    <T> int updateByExample(@Param("record") T record, @Param("example") Object example);

}