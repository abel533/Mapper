package tk.mybatis.mapper.test.example;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.model.CountryExample;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuzh
 */
public class TestSelectByExample {

    @Test
    public void testSelectByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id",151);
            example.or().andLessThan("id",41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleInNotIn() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andIn("id", Arrays.asList(new Object[]{1,2,3,4,5,6,7,8,9,10,11}))
                    .andNotIn("id", Arrays.asList(new Object[]{11}));
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(10, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andLike("countryname", "A%");
            example.or().andGreaterThan("id", 100);
            example.            setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample example = new CountryExample();
            example.createCriteria().andCountrynameLike("A%");
            example.or().andIdGreaterThan(100);
            example.setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assert.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample4() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country ct = new Country();
            ct.setCountryname("China");

            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 20).andEqualTo(ct);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            System.out.println(countries.get(0).toString());
            Assert.assertEquals(1, countries.size());
        } finally {
            sqlSession.close();
        }
    }

}
