package com.github.abel533.sql.test;

import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by liuzh on 2015/3/10.
 */
public class SelectListTest {

    @Test
    public void testSqlHelperSelectList() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            List<Map<String, Object>> list = sqlMapper.selectList("select * from country where id < 11");
            Assert.assertEquals(10, list.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectList2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            List<Country> list = sqlMapper.selectList("select * from country where id < 11", Country.class);
            Assert.assertEquals(10, list.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectList3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            List<Map<String, Object>> list = sqlMapper.selectList("select * from country where id < #{id}", 11);
            Assert.assertEquals(10, list.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectList4() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            List<Country> list = sqlMapper.selectList("select * from country where id < #{id}", 11, Country.class);
            Assert.assertEquals(10, list.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSqlHelperSelectList5() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        try {
            Country country = new Country();
            country.setId(11);
            List<Country> list = sqlMapper.selectList("<script>" +
                    "select * from country " +
                    "   <where>" +
                    "       <if test=\"id != null\">" +
                    "           id &lt; #{id}" +
                    "       </if>" +
                    "   </where>" +
                    "</script>", country, Country.class);
            Assert.assertEquals(10, list.size());
        } finally {
            sqlSession.close();
        }
    }

}
