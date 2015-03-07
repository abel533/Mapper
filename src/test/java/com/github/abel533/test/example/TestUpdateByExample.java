package com.github.abel533.test.example;

import com.github.abel533.entity.Example;
import com.github.abel533.entity.model.CountryExample;
import com.github.abel533.mapper.CountryMapper;
import com.github.abel533.mapper.MybatisHelper;
import com.github.abel533.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 由于主键的原因，这里实际只能更新一个，在没有主键的表中，可以更新多个
 *
 * @author liuzh
 */
public class TestUpdateByExample {

    @Test
    public void testUpdateByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andEqualTo("id", 35);
            Country country = new Country();
            country.setCountryname("天朝");
            country.setId(1000);
            int count = mapper.updateByExample(country, example);
            Assert.assertEquals(1, count);

            example = new Example(Country.class);
            example.createCriteria().andIsNull("countrycode");
            count = mapper.selectCountByExample(example);
            Assert.assertEquals(1, count);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountryname("天朝");
            country.setId(1000);


            CountryExample example = new CountryExample();
            example.createCriteria().andIdEqualTo(35);
            int count = mapper.updateByExample(country, example);
            Assert.assertEquals(1, count);

            example = new CountryExample();
            example.createCriteria().andCountrycodeIsNull();
            count = mapper.selectCountByExample(example);
            Assert.assertEquals(1, count);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

}
