package tk.mybatis.mapper.base.delete;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;

public class SafeDeleteByFieldTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeDelete(true);
        config.setNotEmpty(true);
        return config;
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDelete() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.delete(new Country());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDeleteNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.delete(null);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDeleteEmptyString() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountryname("");
            mapper.delete(country);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDeleteByExample() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.deleteByExample(new Example(Country.class));
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDeleteByExampleNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.deleteByExample(null);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeDeleteByExampleWithEmptyStringCriterion() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andEqualTo("countryname", "");
            mapper.deleteByExample(example);
        } finally {
            sqlSession.close();
        }
    }

}
