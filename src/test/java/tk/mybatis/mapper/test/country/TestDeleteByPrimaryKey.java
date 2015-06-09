package tk.mybatis.mapper.test.country;

import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过主键删除
 *
 * @author liuzh
 */
public class TestDeleteByPrimaryKey {

    /**
     * 主要测试删除
     */
    @Test
    public void testDynamicDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //查询总数
            Assert.assertEquals(183, mapper.selectCount(new Country()));
            //查询100
            Country country = mapper.selectByPrimaryKey(100);
            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(100));
            //查询总数
            Assert.assertEquals(182, mapper.selectCount(new Country()));
            //插入
            Assert.assertEquals(1, mapper.insert(country));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 删除不存在的主键
     */
    @Test
    public void testDynamicDeleteZero() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(null));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(-100));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(0));
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(1000));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 对象包含主键即可
     */
    @Test
    public void testDynamicDeleteEntity() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            Country country = new Country();
            country.setId(100);
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Map可以随意
     */
    @Test
    public void testDynamicDeleteMap() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            Map map = new HashMap();
            map.put("id", 100);
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(map));

            map = new HashMap();
            map.put("countryname", "China");
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(map));
        } finally {
            sqlSession.close();
        }
    }

    class Key {
    }

    /**
     * 对象不包含主键
     */
    @Test(expected = Exception.class)
    public void testDynamicDeleteNotFoundKeyProperties() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(0, mapper.deleteByPrimaryKey(new Key()));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 主键格式错误
     */
    @Test(expected = Exception.class)
    public void testDynamicDeleteException() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //根据主键删除
            Assert.assertEquals(0, mapper.deleteByPrimaryKey("100"));
        } finally {
            sqlSession.close();
        }
    }

}
