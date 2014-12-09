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

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 通用Mapper接口,其他接口继承该接口即可
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @param <T> 不能为空
 * @author liuzh
 */
public interface Mapper<T> {

    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    List<T> select(T record);

    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    int selectCount(T record);

    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    T selectByPrimaryKey(Object key);

    @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
    int insert(T record);

    @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
    int insertSelective(T record);

    @DeleteProvider(type = MapperProvider.class, method = "dynamicSQL")
    int delete(T key);

    @DeleteProvider(type = MapperProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKey(Object key);

    @UpdateProvider(type = MapperProvider.class, method = "dynamicSQL")
    int updateByPrimaryKey(T record);

    @UpdateProvider(type = MapperProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelective(T record);

}
