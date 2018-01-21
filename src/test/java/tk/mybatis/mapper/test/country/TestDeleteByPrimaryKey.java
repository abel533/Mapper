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

package tk.mybatis.mapper.test.country;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.CountryVersionMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.model.CountryVersion;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过主键删除
 *
 * @author liuzh
 */
public class TestDeleteByPrimaryKey {

    /**
     * 主要测试删除
     */
    @Test
    public void testDynamicDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //查询总数
            Assert.assertEquals(183, mapper.selectCount(new Country()));
            //查询100
            Country country = mapper.selectByPrimaryKey(100);
            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(100));
            //查询总数
            Assert.assertEquals(182, mapper.selectCount(new Country()));
            //插入
            Assert.assertEquals(1, mapper.insert(country));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 删除不存在的主键
     */
    @Test
    public void testDynamicDeleteZero() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(null));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(-100));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(0));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(1000));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 对象包含主键即可
     */
    @Test
    public void testDynamicDeleteEntity() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            Country country = new Country();
            country.setId(100);
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Map可以随意
     */
    @Test
    public void testDynamicDeleteMap() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            Map map = new HashMap();
            map.put("id", 100);
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(map));

            map = new HashMap();
            map.put("countryname", "China");
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(map));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 对象不包含主键
     */
    @Test(expected = Exception.class)
    public void testDynamicDeleteNotFoundKeyProperties() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(new Key()));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 主键格式错误
     */
    @Test
    public void testDynamicDeleteException() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(100));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    /**
     * 乐观锁删除
     */
    @Test
    public void testDeleteByVersion() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryVersionMapper mapper = sqlSession.getMapper(CountryVersionMapper.class);
            CountryVersion countryVersion = new CountryVersion();
            countryVersion.setId(100);

            //没有指定版本时删除不了
            Assert.assertEquals(0, mapper.delete(countryVersion));

            //版本不对的时候的时候删除不了
            countryVersion.setVersion(2);
            Assert.assertEquals(0, mapper.delete(countryVersion));

            //版本正确的时候可以真正删除
            countryVersion.setVersion(1);
            Assert.assertEquals(1, mapper.delete(countryVersion));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    /**
     * 乐观锁删除
     */
    @Test
    public void testDeleteByPrimaryKeyAndVersion() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryVersionMapper mapper = sqlSession.getMapper(CountryVersionMapper.class);
            //根据主键删除，没有指定版本时删除不了
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(100));

            CountryVersion countryVersion = new CountryVersion();
            countryVersion.setId(100);

            //版本不对的时候的时候删除不了
            countryVersion.setVersion(2);
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(countryVersion));

            //版本正确的时候可以真正删除
            countryVersion.setVersion(1);
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(countryVersion));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    class Key {
    }

}
