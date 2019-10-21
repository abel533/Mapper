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

package tk.mybatis.mapper.test.mysql;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.PersonMapper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通过实体类属性进行插入
 *
 * @author liuzh
 */
public class TestMysql {

    /**
     * 插入完整数据
     */
    //该方法测试需要mysql或者h2数据库，所以这里注释掉
    //@Test
    public void testInsertList() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> countryList = new ArrayList<Country>();
            for (int i = 0; i < 10; i++) {
                Country country = new Country();
                country.setCountrycode("CN" + i);
                country.setCountryname("天朝" + i);
                countryList.add(country);
            }
            int count = mapper.insertList(countryList);
            Assert.assertEquals(10, count);
            for (Country country : countryList) {
                Assert.assertNotNull(country.getId());
            }
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    //该方法测试需要mysql或者h2数据库，所以这里注释掉
    //@Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            int count = mapper.insertUseGeneratedKeys(country);
            Assert.assertEquals(1, count);
            Assert.assertNotNull(country.getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    /**
     * 插入完整数据
     */
    //该方法测试需要mysql数据库，所以这里注释掉
    @Test
    public void testInsertListSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
            List<Person> personList = new ArrayList<Person>();
            personList.add(new Person());
            personList.add(new Person().setName("test").setCtime(new Date()));
            personMapper.insertListSelective(personList);
            List<Person> list = personMapper.selectAll();
            System.out.println(list);
            Assert.assertEquals(list.size(), 2);
            Person person0 = list.get(0);
            Assert.assertEquals(person0.getName(), "");
            Assert.assertNotNull(person0.getCtime());
            Person person1 = list.get(1);
            Assert.assertEquals(person1.getName(), "test");
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
