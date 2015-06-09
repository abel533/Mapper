package tk.mybatis.mapper.test.country;

import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 通过PK更新实体类非null属性
 *
 * @author liuzh
 */
public class TestUpdateByPrimaryKeySelective {

    /**
     * set属性为0,导致异常
     */
    @Test(expected = Exception.class)
    //TODO 测试手写的是否存在这个问题
    public void testDynamicUpdateByPrimaryKeySelectiveAll() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByPrimaryKeySelective(new Country());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 除了通过主键的方法，其他的方法入参不能为null
     */
    @Test(expected = RuntimeException.class)
    public void testDynamicUpdateByPrimaryKeySelectiveAllByNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByPrimaryKeySelective(null);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setId(173);
            country.setCountryname("英国");
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(country));

            country = mapper.selectByPrimaryKey(173);
            Assert.assertNotNull(country);
            Assert.assertEquals(173, (int) country.getId());
            Assert.assertEquals("英国", country.getCountryname());
            Assert.assertNotNull(country.getCountrycode());
            Assert.assertEquals("GB", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    class Key extends Country {
        private String countrytel;

        public String getCountrytel() {
            return countrytel;
        }

        public void setCountrytel(String countrytel) {
            this.countrytel = countrytel;
        }
    }

    /**
     * 继承类可以使用,但多出来的属性无效
     */
    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveNotFoundKeyProperties() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Key key = new Key();
            key.setId(173);
            key.setCountrycode("CN");
            key.setCountrytel("+86");
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(key));
        } finally {
            sqlSession.close();
        }
    }

}
