/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
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

package tk.mybatis.mapper.test.example;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.StringTypeHandler;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.model.CountryExample;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.model.Country2;

import java.util.*;

/**
 * @author liuzh
 */
public class TestSelectByExample {

    @Test
    public void testSelectByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id",151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = Exception.class)
    public void testSelectByExampleException() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country2.class);
            example.createCriteria().andGreaterThan("id", 100);
            mapper.selectByExample(example);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleForUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.setForUpdate(true);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id",151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAndExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria()
                    .andCondition("countryname like 'C%' and id < 100")
                    .andCondition("length(countryname) = ", 5)
                    .andCondition("countrycode =", "CN", StringTypeHandler.class);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(1, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleInNotIn() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            Set<Integer> set = new HashSet<Integer>();
            set.addAll(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
            example.createCriteria().andIn("id", set)
                    .andNotIn("id", Arrays.asList(new Object[]{11}));
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(10, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleInNotIn2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andIn("id", Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}))
                    .andNotIn("id", Arrays.asList(new Object[]{11}));
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(10, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andLike("countryname", "A%");
            example.or().andGreaterThan("id", 100);
            example.            setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample example = new CountryExample();
            example.createCriteria().andCountrynameLike("A%");
            example.or().andIdGreaterThan(100);
            example.setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample4() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country ct = new Country();
            ct.setCountryname("China");

            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 20).andEqualTo(ct);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            System.out.println(countries.get(0).toString());
            Assert.assertEquals(1, countries.size());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectColumnsByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            example.selectProperties("id", "countryname", "hehe");
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testExcludeColumnsByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            example.excludeProperties("id");
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAndOr() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());

            //当不使用条件时，也不能出错
            example = new Example(Country.class);
            example.createCriteria();
            example.or();
            example.and();
            countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(183, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testOrderBy() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
//            example.setOrderByClause("id desc");
            example.orderBy("id").desc().orderBy("countryname").orderBy("countrycode").asc();
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(183, (int) countries.get(0).getId());
        } finally {
            sqlSession.close();
        }
    }

}
