package tk.mybatis.mapper.test.weekend;

/*
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
*/

/**
 * 测试WeekendSql构建者模式类 由于方法引用需要jdk8
 * 执行该测试的时候需要临时将pom.xml中maven-compiler-plugin插件jdk编译等级调整为1.8
 * 为了防止jdk6编译等级打包出错，将测试用例全部注释
 * <p>
 * <pre>
 * 		<plugin>
 * <artifactId>maven-compiler-plugin</artifactId>
 * <configuration>
 * <source>1.8</source>
 * <target>1.8</target>
 * </configuration>
 * </plugin>
 * </pre>
 *
 * @author XuYin
 */
public class WeekendSqlsTest {
    //@Test
    /*
    public void testWeekend() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> selectByExample = mapper.selectByExample(
                    new Example.Builder(Country.class).where(Sqls.custom().andLike("countryname", "China")).build());
            List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "China")).build());
            // 判断两个结果数组内容是否相同
            Assert.assertArrayEquals(selectByExample.toArray(), selectByWeekendSql.toArray());
        } finally {
            sqlSession.close();
        }
    }

    //@Test
    public void testWeekendComplex() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> selectByExample = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(Sqls.custom().andLike("countryname", "%a%").andGreaterThan("countrycode", "123")).build());
            List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "%a%")
                            .andGreaterThan(Country::getCountrycode, "123"))
                    .build());
            // 判断两个结果数组内容是否相同
            Assert.assertArrayEquals(selectByExample.toArray(), selectByWeekendSql.toArray());
        } finally {
            sqlSession.close();
        }
    }
    */
}
