package com.github.abel533.test.uuid;

import com.github.abel533.mapper.CountryUMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.CountryU;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzh on 2014/11/21.
 */
public class TestUUID {
    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryUMapper mapper = sqlSession.getMapper(CountryUMapper.class);
            CountryU country = new CountryU();
            country.setId(10086);
            country.setCountrycode("CN");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryU();
            country.setCountrycode("CN");
            List<CountryU> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            Assert.assertNotNull(list.get(0).getCountryname());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsertAll() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryUMapper mapper = sqlSession.getMapper(CountryUMapper.class);
            CountryU country = new CountryU();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryU();
            country.setCountrycode("CN");
            List<CountryU> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            Assert.assertNotNull(list.get(0).getCountryname());
            Assert.assertEquals("天朝",list.get(0).getCountryname());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }
}
