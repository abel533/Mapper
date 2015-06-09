package tk.mybatis.mapper.test.country;

import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 通过实体类属性进行查询
 *
 * @author liuzh
 */
public class TestSelectOne {

    /**
     * 查询全部
     */
    @Test(expected = TooManyResultsException.class)
    public void testDynamicSelectAll() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = mapper.selectOne(new Country());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 入参为null时查询全部
     */
    @Test(expected = TooManyResultsException.class)
    public void testDynamicSelectAllByNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.selectOne(null);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicSelect() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            Country result = mapper.selectOne(country);

            Assert.assertEquals(true, result.getId() == 35);
            Assert.assertEquals("China", result.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询不存在的结果
     */
    @Test
    public void testDynamicSelectZero() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            country.setCountryname("天朝");//实际上是 China
            Country result = mapper.selectOne(country);

            Assert.assertNull(result);
        } finally {
            sqlSession.close();
        }
    }

}
