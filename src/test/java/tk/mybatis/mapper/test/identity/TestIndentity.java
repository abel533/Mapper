package tk.mybatis.mapper.test.identity;

import tk.mybatis.mapper.mapper.CountryIMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.CountryI;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzh on 2014/11/21.
 */
public class TestIndentity {
    /**
     * 插入完整数据
     */
    @Test
    public void testINDENTITYInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIMapper mapper = sqlSession.getMapper(CountryIMapper.class);
            CountryI country = new CountryI();
            country.setCountrycode("CN");
            Assert.assertEquals(1, mapper.insert(country));
            //ID会回写
            Assert.assertNotNull(country.getId());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country.getId()));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testINDENTITYInsert2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIMapper mapper = sqlSession.getMapper(CountryIMapper.class);
            CountryI country = new CountryI();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryI();
            country.setCountrycode("CN");
            List<CountryI> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            Assert.assertNotNull(list.get(0).getCountryname());
            Assert.assertEquals("天朝", list.get(0).getCountryname());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testINDENTITYInsertSelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIMapper mapper = sqlSession.getMapper(CountryIMapper.class);
            CountryI country = new CountryI();
            Assert.assertEquals(1, mapper.insertSelective(country));
            //ID会回写
            Assert.assertNotNull(country.getId());
            //带有默认值的其他的属性不会自动回写,需要手动查询
            country = mapper.selectByPrimaryKey(country);
            //查询后,默认值不为null
            Assert.assertNotNull(country.getCountrycode());
            Assert.assertEquals("HH", country.getCountrycode());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(country.getId()));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入完整数据
     */
    @Test
    public void testINDENTITYInsertSelective2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIMapper mapper = sqlSession.getMapper(CountryIMapper.class);
            CountryI country = new CountryI();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insertSelective(country));

            //查询CN结果
            country = new CountryI();
            country.setCountrycode("CN");
            List<CountryI> list = mapper.select(country);

            Assert.assertEquals(1, list.size());
            Assert.assertNotNull(list.get(0).getCountryname());
            Assert.assertEquals("天朝", list.get(0).getCountryname());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }
}
