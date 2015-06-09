package tk.mybatis.mapper.test.country;

import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 通过实体类属性查询总数
 *
 * @author liuzh
 */
public class TestSelectCount {

    /**
     * 查询全部
     */
    @Test
    public void testDynamicSelectCount() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //查询总数
            Assert.assertEquals(183, mapper.selectCount(new Country()));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 入参为null时查询全部
     */
    @Test
    public void testDynamicSelectAllByNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //查询总数
            Assert.assertEquals(183, mapper.selectCount(null));
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
            Assert.assertEquals(1, mapper.selectCount(country));
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
            Assert.assertEquals(0, mapper.selectCount(country));
        } finally {
            sqlSession.close();
        }
    }

}
