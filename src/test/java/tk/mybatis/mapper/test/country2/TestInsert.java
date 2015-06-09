package tk.mybatis.mapper.test.country2;

import tk.mybatis.mapper.mapper.Country2Mapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 通过实体类属性进行插入 - Country2 自动增长ID
 *
 * @author liuzh
 */
public class TestInsert {

    /**
     * 插入空数据,id不能为null,会报错
     */
    @Test
    public void testDynamicInsertAll() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            Country2 country2 = new Country2();
            country2.setCountrycode("CN");
            Assert.assertEquals(1, mapper.insert(country2));

            country2 = mapper.select(country2).get(0);
            Assert.assertNotNull(country2);

            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country2.getId()));

        } finally {
            sqlSession.close();
        }
    }

    /**
     * 不能插入null
     */
    @Test
    public void testDynamicInsertAllByNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            mapper.insert(null);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            Country2 country = new Country2();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果2个
            country = new Country2();
            country.setCountrycode("CN");
            List<Country2> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入code为null的数据,不会使用默认值HH
     */
    @Test
    public void testDynamicInsertNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            Country2 country = new Country2();
            country.setId(10086);
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果2个
            country = new Country2();
            country.setId(10086);
            List<Country2> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            Assert.assertNull(list.get(0).getCountrycode());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

}
