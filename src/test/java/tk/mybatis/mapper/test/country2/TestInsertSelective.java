package tk.mybatis.mapper.test.country2;

import tk.mybatis.mapper.mapper.Country2Mapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country2;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 通过实体类属性进行插入不为null的数据
 *
 * @author liuzh
 */
public class TestInsertSelective {

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
            Assert.assertEquals(1, mapper.insertSelective(country2));

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
    @Test(expected = PersistenceException.class)
    public void testDynamicInsertSelectiveAllByNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            mapper.insertSelective(null);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsertSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            Country2 country = new Country2();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insertSelective(country));

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
     * Country2code默认值HH
     */
    @Test
    public void testDynamicInsertSelectiveNull() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country2Mapper mapper = sqlSession.getMapper(Country2Mapper.class);
            Country2 country = new Country2();
            country.setId(10086);
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insertSelective(country));

            //查询CN结果2个
            country = new Country2();
            country.setId(10086);
            List<Country2> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            //默认值
            Assert.assertNotNull(list.get(0).getCountrycode());
            Assert.assertEquals("HH",list.get(0).getCountrycode());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

}
