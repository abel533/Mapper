package com.github.abel533.test.mysql;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import org.apache.ibatis.exceptions.PersistenceException;
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
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> countryList = new ArrayList<Country>();
            for (int i = 0; i < 10; i++) {
                Country country = new Country();
                country.setId(10086 + i);
                country.setCountrycode("CN" + i);
                country.setCountryname("天朝" + i);
                countryList.add(country);
            }
            int count = mapper.insertList(countryList);
            Assert.assertEquals(10, count);


            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThanOrEqualTo("id",10086);
            List<Country> list = mapper.selectByExample(example);

            Assert.assertEquals("CN2",list.get(2).getCountrycode());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
