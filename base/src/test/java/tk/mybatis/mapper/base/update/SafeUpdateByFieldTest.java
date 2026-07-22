package tk.mybatis.mapper.base.update;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;

public class SafeUpdateByFieldTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeUpdate(true);
        return config;
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateByPrimaryKeyNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByPrimaryKey(new Country());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateByPrimaryKeySelectiveNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByPrimaryKeySelective(new Country());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdate() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByExample(new Country(), new Example(Country.class));
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateWithEmptyStringCriterion() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andEqualTo("countryname", "");
            mapper.updateByExample(new Country(), example);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByExample(new Country(), null);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateNull2() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByExample(null, null);
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateByExample() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByExampleSelective(new Country(), new Example(Country.class));
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSafeUpdateByExampleNull() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.updateByExampleSelective(new Country(), null);
        } finally {
            sqlSession.close();
        }
    }

}
