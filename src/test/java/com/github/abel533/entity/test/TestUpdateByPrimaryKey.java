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

package com.github.abel533.entity.test;

import com.github.abel533.entity.EntityMapper;
import com.github.abel533.entity.mapper.CommonMapper;
import com.github.abel533.entity.model.Country;
import com.github.abel533.mapper.MybatisHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试更新
 *
 * @author liuzh
 */
public class TestUpdateByPrimaryKey {

    @Test
    public void testUpdateByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CommonMapper commonMapper = sqlSession.getMapper(CommonMapper.class);
            EntityMapper entityMapper = new EntityMapper(commonMapper);
            Country country = new Country();
            country.setId(100);
            country.setCountryname("测试");
            int count = entityMapper.updateByPrimaryKey(country);
            Assert.assertEquals(1, count);

            country = entityMapper.selectByPrimaryKey(Country.class, 100);
            Assert.assertNull(country.getCountrycode());
            Assert.assertEquals("测试", country.getCountryname());
        } finally {
            //回滚
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test(expected = Exception.class)
    public void testUpdateNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CommonMapper commonMapper = sqlSession.getMapper(CommonMapper.class);
            EntityMapper entityMapper = new EntityMapper(commonMapper);

            entityMapper.updateByPrimaryKey(null);
        } finally {
            sqlSession.close();
        }
    }
}
