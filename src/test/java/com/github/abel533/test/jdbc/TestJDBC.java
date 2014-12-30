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

package com.github.abel533.test.jdbc;

import com.github.abel533.mapper.CountryJDBCMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.CountryJDBC;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzh on 2014/11/21.
 */
public class TestJDBC {
    /**
     * 插入完整数据
     */
    @Test
    public void testJDBC() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryJDBCMapper mapper = sqlSession.getMapper(CountryJDBCMapper.class);
            CountryJDBC country = new CountryJDBC();
            country.setId(10086);
            country.setCountrycode("CN");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryJDBC();
            country.setCountrycode("CN");
            List<CountryJDBC> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testJDBC2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryJDBCMapper mapper = sqlSession.getMapper(CountryJDBCMapper.class);
            CountryJDBC country = new CountryJDBC();
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country.getId()));
        } finally {
            sqlSession.close();
        }
    }
}
