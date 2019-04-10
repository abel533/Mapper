package tk.mybatis.mapper.additional.idlist;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;
import tk.mybatis.mapper.additional.Country;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdListMapperTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        //安全删除
        config.setSafeDelete(true);
        return config;
    }

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    @Test
    public void testByIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Long> idList = Arrays.asList(1L, 2L, 3L);
            List<Country> countryList = mapper.selectByIdList(idList);
            Assert.assertEquals(3, countryList.size());
            Assert.assertEquals(1L, (long) countryList.get(0).getId());
            Assert.assertEquals(2L, (long) countryList.get(1).getId());
            Assert.assertEquals(3L, (long) countryList.get(2).getId());
            //删除
            Assert.assertEquals(3, mapper.deleteByIdList(idList));
            //查询结果0
            Assert.assertEquals(0, mapper.selectByIdList(idList).size());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = Exception.class)
    public void testDeleteByEmptyIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.deleteByIdList(new ArrayList<Long>());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByEmptyIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Assert.assertEquals(183, mapper.selectByIdList(new ArrayList<Long>()).size());
        } finally {
            sqlSession.close();
        }
    }

}
