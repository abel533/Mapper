package com.github.abel533.sql.test;

import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuzh on 2015/3/10.
 */
public class InsertTest {

    @Test
    public void testSqlHelperInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            int result = sqlMapper.insert("insert into country values(1921,'天朝','TC')");
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(1921);
            Assert.assertNotNull(country);
            Assert.assertEquals("天朝", country.getCountryname());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperInsert2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //sqlMapper插入
            Country tc = new Country();
            tc.setId(1921);
            tc.setCountryname("天朝");
            tc.setCountrycode("TC");
            //注意这里的countrycode和countryname故意写反的
            int result = sqlMapper.insert("insert into country values(#{id},#{countrycode},#{countryname})", tc);
            Assert.assertEquals(1, result);
            //查询验证
            Country country = countryMapper.selectByPrimaryKey(1921);
            Assert.assertNotNull(country);
            Assert.assertEquals("TC", country.getCountryname());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
