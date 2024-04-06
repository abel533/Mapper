package tk.mybatis.mapper.helper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.mapper.CountryMultipleMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

import java.util.List;

/**
 * @author yuanhao
 */
public class MultipleMapperProviderTest {
    @Test
    public void test() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMultipleMapper mapper = sqlSession.getMapper(CountryMultipleMapper.class);
            Country country = new Country();
            country.setId(200);
            country.setCountrycode("AB");
            mapper.insert(country);
            List<Country> countryList = mapper.select(country);
            Assert.assertEquals("AB", countryList.get(0).getCountrycode());
        } finally {
            sqlSession.close();
        }
    }
}
