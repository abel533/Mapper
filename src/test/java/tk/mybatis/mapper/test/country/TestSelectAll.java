package tk.mybatis.mapper.test.country;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;

import java.util.List;

/**
 * 通过实体类属性进行查询
 *
 * @author liuzh
 */
public class TestSelectAll {

    /**
     * 查询全部
     */
    @Test
    public void testDynamicSelectPage() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            List<Country> countryList = mapper.selectAll();
            //查询总数
            Assert.assertEquals(183, countryList.size());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询全部
     */
    @Test
    public void testDynamicSelectPage2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            List<Country> countryList = mapper.selectAll();
            //查询总数
            Assert.assertEquals(183, countryList.size());
            //selectAll有排序
            Assert.assertEquals(183, (int) countryList.get(0).getId());
        } finally {
            sqlSession.close();
        }
    }
}
