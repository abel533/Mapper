package tk.mybatis.mapper.cache;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;

import java.io.IOException;
import java.io.Reader;

/**
 * @author liuzh
 */
public class CacheTest extends BaseTest {

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(CacheTest.class.getResource("mybatis-config-cache.xml"));
    }

    @Test
    public void testNoCache() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //由于 CountryMapper 没有使用二级缓存，因此下面的设置不会影响下次（不同的 SqlSession）查询
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = mapper.selectByPrimaryKey(35);

            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());

            Assert.assertNotEquals("中国", country.getCountryname());
            Assert.assertNotEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSingleInterfaceCache() {
        //利用二级缓存的脏数据特性来验证二级缓存
        SqlSession sqlSession = getSqlSession();
        try {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("中国", country.getCountryname());
            Assert.assertEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
        //下面清空缓存再试
        sqlSession = getSqlSession();
        try {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testCountryCacheRefMapper() {
        //--------------------selectByPrimaryKey---------------------
        //利用二级缓存的脏数据特性来验证二级缓存
        SqlSession sqlSession = getSqlSession();
        try {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("中国", country.getCountryname());
            Assert.assertEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }

        //--------------------selectById---------------------
        sqlSession = getSqlSession();
        try {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectById(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectById(35);
            Assert.assertEquals("中国", country.getCountryname());
            Assert.assertEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
        //下面清空缓存再试
        sqlSession = getSqlSession();
        try {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectById(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    @Ignore("MyBatis 有 Bug，这种方式目前行不通")
    public void testCountryCacheWithXmlMapper() {
        //--------------------selectByPrimaryKey---------------------
        //利用二级缓存的脏数据特性来验证二级缓存
        SqlSession sqlSession = getSqlSession();
        try {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assert.assertEquals("中国", country.getCountryname());
            Assert.assertEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }

        //--------------------selectById---------------------
        sqlSession = getSqlSession();
        try {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectById(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        } finally {
            sqlSession.close();
        }
        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        sqlSession = getSqlSession();
        try {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectById(35);
            Assert.assertEquals("中国", country.getCountryname());
            Assert.assertEquals("ZH", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
        //下面清空缓存再试
        sqlSession = getSqlSession();
        try {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectById(35);
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

}
