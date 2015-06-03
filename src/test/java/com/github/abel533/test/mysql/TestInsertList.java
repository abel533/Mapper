package com.github.abel533.test.mysql;

import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过实体类属性进行插入
 *
 * @author liuzh
 */
public class TestInsertList {

    /**
     * 插入完整数据
     */
    //该方法测试需要mysql或者h2数据库，所以这里注释掉
    @Test
    public void testInsert() {
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
}
