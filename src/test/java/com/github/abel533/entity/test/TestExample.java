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
import com.github.abel533.entity.Example;
import com.github.abel533.entity.mapper.CommonMapper;
import com.github.abel533.entity.model.Country;
import com.github.abel533.mapper.MybatisHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 通过实体类属性进行查询 - 这是目前有的一个测试
 *
 * @author liuzh
 */
public class TestExample {

    /**
     * 这个测试用BeanMapper包装的CommonMapper，select查询返回的bean
     */
    @Test
    public void testCountByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CommonMapper entityMapper = sqlSession.getMapper(CommonMapper.class);
            EntityMapper baseMapper = new EntityMapper(entityMapper);

            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThanOrEqualTo("id", 150);

            int count = baseMapper.countByExample(example);
            Assert.assertEquals(50, count);

            example = new Example(Country.class);
            example.createCriteria().andLike("countryname","A%");

            count = baseMapper.countByExample(example);
            Assert.assertEquals(12, count);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 这个测试用BeanMapper包装的CommonMapper，select查询返回的bean
     */
    @Test
    public void testSelectByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CommonMapper entityMapper = sqlSession.getMapper(CommonMapper.class);
            EntityMapper baseMapper = new EntityMapper(entityMapper);

            Example example = new Example(Country.class);
            example.createCriteria().andEqualTo("countryname","China");

            List<Country> countries = baseMapper.selectByExample(example);
            Assert.assertEquals(1,countries.size());
            Assert.assertEquals("CN",countries.get(0).getCountrycode());

            example = new Example(Country.class);
            example.createCriteria().andEqualTo("id",100);

            countries = baseMapper.selectByExample(example);
            Assert.assertEquals(1,countries.size());
            Assert.assertEquals("MY",countries.get(0).getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

}
