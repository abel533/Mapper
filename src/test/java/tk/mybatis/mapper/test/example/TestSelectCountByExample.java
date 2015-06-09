package tk.mybatis.mapper.test.example;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.model.CountryExample;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuzh
 */
public class TestSelectCountByExample {

    @Test
    public void testSelectCountByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100);
            int count = mapper.selectCountByExample(example);
            //查询总数
            Assert.assertEquals(83, count);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectCountByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andLike("countryname", "A%");
            example.or().andGreaterThan("id", 100);
            example.setDistinct(true);
            int count = mapper.selectCountByExample(example);
            //查询总数
            Assert.assertEquals(true, count > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectCountByExample3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample example = new CountryExample();
            example.createCriteria().andCountrynameLike("A%");
            example.or().andIdGreaterThan(100);
            example.setDistinct(true);
            int count = mapper.selectCountByExample(example);
            //查询总数
            Assert.assertEquals(true, count > 83);
        } finally {
            sqlSession.close();
        }
    }

}
