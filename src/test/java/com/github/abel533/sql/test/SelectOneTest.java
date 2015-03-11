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

package com.github.abel533.sql.test;

import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liuzh on 2015/3/10.
 */
public class SelectOneTest {

    @Test
    public void testSqlHelperSelectOne() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Map<String, Object> map = sqlMapper.selectOne("select * from country where id = 35");
            Assert.assertEquals("China", map.get("COUNTRYNAME"));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectOne2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = sqlMapper.selectOne("select * from country where id = 35", Country.class);
            Assert.assertEquals("China", country.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectOne3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Map<String, Object> map = sqlMapper.selectOne("select * from country where id = #{id}", 35);
            Assert.assertEquals("China", map.get("COUNTRYNAME"));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectOne4() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = sqlMapper.selectOne("select * from country where id = #{id}", 35, Country.class);
            Assert.assertEquals("China", country.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = TooManyResultsException.class)
    public void testSqlHelperSelectOne5() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Map<String, Object> map = sqlMapper.selectOne("select * from country where id < 35");
            Assert.assertEquals("China", map.get("COUNTRYNAME"));
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = TooManyResultsException.class)
    public void testSqlHelperSelectOne6() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = sqlMapper.selectOne("select * from country where id < 35", Country.class);
            Assert.assertEquals("China", country.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = TooManyResultsException.class)
    public void testSqlHelperSelectOne7() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Map<String, Object> map = sqlMapper.selectOne("select * from country where id < #{id}", 35);
            Assert.assertEquals("China", map.get("COUNTRYNAME"));
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = TooManyResultsException.class)
    public void testSqlHelperSelectOne8() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = sqlMapper.selectOne("select * from country where id < #{id}", 35, Country.class);
            Assert.assertEquals("China", country.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = TooManyResultsException.class)
    public void testSqlHelperSelectOne9() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = new Country();
            country.setId(35);
            Country result = sqlMapper.selectOne("<script>" +
                    "select * from country " +
                    "   <where>" +
                    "       <if test=\"id != null\">" +
                    "           id &lt; #{id}" +
                    "       </if>" +
                    "   </where>" +
                    "</script>", country, Country.class);
            Assert.assertEquals("China", result.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

}
