package tk.mybatis.mapper.weekend;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.entity.Country;
import tk.mybatis.mapper.weekend.mapper.CountryMapper;

import java.util.List;

/**
 * @author Cheng.Wei
 */
public class SqlCriteriaHelperTest {
    /**
     * 忽略null值问题
     */
    @Test
    public void ignore() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            List<Country> selectBySqlCriteriaHelper= mapper.selectByExample(new Example.Builder(Country.class)
                    .where(SqlCriteriaHelper.custom(Country.class)
                            .andEqualTo(Country::getCountryname, null)
                            .andLike(Country::getCountryname, "China")).build());

            List<Country> selectByWeekendSqls = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom()
                            .andEqualTo(Country::getCountryname, null)
                            .andLike(Country::getCountrycode, "China")).build());
        } finally {
            sqlSession.close();
        }
    }


    /**
     * 不忽略null属性
     * 当属性为null 且不忽略 则转换查询 equal null 转 is null
     */
    @Test
    public void required() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            List<Country> selectBySqlCriteriaHelper= mapper.selectByExample(new Example.Builder(Country.class)
                    .where(SqlCriteriaHelper.custom(Country.class)
                            // required = true 则继续查询
                            .andEqualTo(Country::getCountryname, null, true)).build());

            List<Country> selectByWeekendSqls = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom()
                            .andEqualTo(Country::getCountryname, null)).build());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * like查询 自动拼接 %
     */
    @Test
    public void like() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            List<Country> selectBySqlCriteriaHelper= mapper.selectByExample(new Example.Builder(Country.class)
                    .where(SqlCriteriaHelper.custom(Country.class)
                            .andLike(Country::getCountryname, "Chin")
                            .orLike(Country::getCountryname, "A")).build());

            List<Country> selectByWeekendSqls = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom()
                            .andLike(Country::getCountryname, "Chin")
                            .orLike(Country::getCountryname, "A")).build());
            //判断两个结果数组内容是否相同
            Assert.assertArrayEquals(selectBySqlCriteriaHelper.toArray(), selectByWeekendSqls.toArray());
        } finally {
            sqlSession.close();
        }
    }
}
