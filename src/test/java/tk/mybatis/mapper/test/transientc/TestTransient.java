package tk.mybatis.mapper.test.transientc;

import tk.mybatis.mapper.mapper.CountryTMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.CountryT;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuzh on 2014/11/21.
 */
public class TestTransient {
    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assert.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryT();
            country.setCountrycode("CN");
            List<CountryT> list = mapper.select(country);

            Assert.assertEquals(2, list.size());
            //屏蔽的数据是null
            Assert.assertNull(list.get(0).getCountrycode());
            //删除插入的数据,以免对其他测试产生影响
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicUpdateByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(174);
            country.setCountryname("美国");
            country.setCountrycode("US");
            Assert.assertEquals(1, mapper.updateByPrimaryKey(country));

            country = mapper.selectByPrimaryKey(174);
            Assert.assertNotNull(country);
            Assert.assertEquals(174, (int) country.getId());
            Assert.assertEquals("美国",country.getCountryname());
            Assert.assertNull(country.getCountrycode());
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
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(174);
            country.setCountrycode("US");
            List<CountryT> countryList = mapper.select(country);

            Assert.assertEquals(1, countryList.size());
            Assert.assertEquals(true, countryList.get(0).getId() == 174);
            Assert.assertNotNull(countryList.get(0).getCountryname());
            Assert.assertNull(countryList.get(0).getCountrycode());
        } finally {
            sqlSession.close();
        }
    }
}
