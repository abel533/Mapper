package com.github.abel533.sql.test;

import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzh on 2015/3/10.
 */
public class UpdateTest {

    @Test
    public void testSqlHelperUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            int result = sqlMapper.update("update country set countryname = '天朝' where id = 35");
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(35);
            Assert.assertNotNull(country);
            Assert.assertEquals("天朝", country.getCountryname());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperUpdate2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            Country tc = new Country();
            tc.setId(35);
            tc.setCountryname("天朝");
            int result = sqlMapper.update("update country set countryname = #{countryname} where id = ${id}", tc);
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(35);
            Assert.assertNotNull(country);
            Assert.assertEquals("天朝", country.getCountryname());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperUpdate3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            Country tc = new Country();
            tc.setId(35);
            tc.setCountryname("天朝");
            //注意这里的countrycode和countryname故意写反的
            int result = sqlMapper.update("update country set countryname = #{countryname} where id in(select id from country where countryname like 'A%')", tc);
            Assert.assertEquals(12, result);
            tc = new Country();
            tc.setCountryname("天朝");
            List<Country> countryList = countryMapper.select(tc);
            Assert.assertEquals(12, countryList.size());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
