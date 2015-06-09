package tk.mybatis.mapper.test.example;

import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.model.CountryExample;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuzh
 */
public class TestUpdateByExampleSelective {

    @Test
    public void testUpdateByExampleSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100);
            Country country = new Country();
            country.setCountryname("天朝");
            int count = mapper.updateByExampleSelective(country, example);
            Assert.assertEquals(83, count);

            example = new Example(Country.class);
            example.createCriteria().andEqualTo("countryname","天朝");
            count = mapper.selectCountByExample(example);
            Assert.assertEquals(83, count);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExampleSelective2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andLike("countryname", "A%");
            example.or().andGreaterThan("id", 100);
            example.setDistinct(true);
            Country country = new Country();
            country.setCountryname("天朝");
            int count = mapper.updateByExampleSelective(country, example);
            Assert.assertEquals(true, count > 83);

            example = new Example(Country.class);
            example.createCriteria().andEqualTo("countryname","天朝");
            count = mapper.selectCountByExample(example);
            Assert.assertEquals(true, count > 83);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExampleSelective3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample example = new CountryExample();
            example.createCriteria().andCountrynameLike("A%");
            example.or().andIdGreaterThan(100);
            example.setDistinct(true);
            Country country = new Country();
            country.setCountryname("天朝");
            int count = mapper.updateByExampleSelective(country, example);
            Assert.assertEquals(true, count > 83);

            example = new CountryExample();
            example.createCriteria().andCountrynameEqualTo("天朝");
            count = mapper.selectCountByExample(example);
            Assert.assertEquals(true, count > 83);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

}
