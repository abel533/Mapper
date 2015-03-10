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
public class DeleteTest {

    @Test
    public void testSqlHelperDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            int result = sqlMapper.delete("delete from country where id = 35");
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(35);
            Assert.assertNull(country);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperDelete2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            int result = sqlMapper.delete("delete from country where id = #{id}", 35);
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(35);
            Assert.assertNull(country);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperDelete33() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            Country tc = new Country();
            tc.setId(35);
            tc.setCountryname("天朝");
            tc.setCountrycode("TC");
            int result = sqlMapper.delete("delete from country where id = #{id}", tc);
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(35);
            Assert.assertNull(country);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testSqlHelperDelete3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            Country tc = new Country();
            tc.setId(35);
            tc.setCountryname("天朝");
            //注意这里的countrycode和countryname故意写反的
            int result = sqlMapper.delete("delete from country where id in(select id from country where countryname like 'A%')", tc);
            Assert.assertEquals(12, result);
            tc = new Country();
            tc.setCountryname("天朝");
            List<Country> countryList = countryMapper.select(tc);
            Assert.assertEquals(0, countryList.size());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
