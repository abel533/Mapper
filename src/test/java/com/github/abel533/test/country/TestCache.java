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

package com.github.abel533.test.country;

import com.github.abel533.mapper.CachedCountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 通过实体类属性进行查询
 *
 * @author liuzh
 */
public class TestCache {

    @Test
    public void testCache() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CachedCountryMapper mapper = sqlSession.getMapper(CachedCountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            //第一次查询，会被缓存
            country = mapper.selectOne(country);
            Assert.assertEquals(true, country.getId() == 35);
            Assert.assertEquals("China", country.getCountryname());
            //只有close才会真正缓存，而不是用一级缓存
            sqlSession.close();

            //======================================================================
            sqlSession = MybatisHelper.getSqlSession();
            mapper = sqlSession.getMapper(CachedCountryMapper.class);
            country = new Country();
            country.setCountrycode("CN");
            //第二次查询，会使用第一次的缓存
            country = mapper.selectOne(country);

            Assert.assertEquals(true, country.getId() == 35);
            Assert.assertEquals("China", country.getCountryname());
            //只有close才会真正缓存，而不是用一级缓存
            sqlSession.close();

            //======================================================================
            sqlSession = MybatisHelper.getSqlSession();
            mapper = sqlSession.getMapper(CachedCountryMapper.class);

            country = new Country();
            country.setCountryname("天朝");
            country.setId(35);
            //更新操作会清空缓存
            int result = mapper.updateByPrimaryKeySelective(country);
            Assert.assertEquals(1, result);
            sqlSession.commit();
            //只有close才会真正缓存，而不是用一级缓存
            sqlSession.close();

            //======================================================================
            sqlSession = MybatisHelper.getSqlSession();
            mapper = sqlSession.getMapper(CachedCountryMapper.class);
            country = new Country();
            country.setCountrycode("CN");
            //第三次查询，会重新查询，不使用缓存
            country = mapper.selectOne(country);

            Assert.assertEquals(true, country.getId() == 35);
            Assert.assertEquals("天朝", country.getCountryname());

            country = new Country();
            country.setCountryname("China");
            country.setId(35);
            //还原
            result = mapper.updateByPrimaryKeySelective(country);
            sqlSession.commit();
            Assert.assertEquals(1, result);
        } finally {
            sqlSession.close();
        }
    }
}
