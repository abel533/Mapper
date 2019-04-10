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

package tk.mybatis.mapper.test.jdbc;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import tk.mybatis.mapper.mapper.CountryJDBCMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.CountryJDBC;

/**
 * sqlserver测试 - 该类注释所有测试方法是因为该测试是针对sqlserver的，而项目测试用的hsqldb，所以这些测试不能运行，需要换为sqlserver才可以
 */
public class TestJDBC {

//    @Test
    public void testJDBC() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryJDBCMapper mapper = sqlSession.getMapper(CountryJDBCMapper.class);
            CountryJDBC country = new CountryJDBC();
            country.setId(1);
            country.setCountrycode("CN");
            country.setCountryname("China");
            Assert.assertEquals(1, mapper.insert(country));
            Assert.assertNotNull(country.getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

//    @Test
    public void testJDBC2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryJDBCMapper mapper = sqlSession.getMapper(CountryJDBCMapper.class);
            CountryJDBC country = new CountryJDBC();
            country.setId(1);
            country.setCountrycode("CN");
            country.setCountryname("China");
            Assert.assertEquals(1, mapper.insertSelective(country));
            Assert.assertNotNull(country.getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
