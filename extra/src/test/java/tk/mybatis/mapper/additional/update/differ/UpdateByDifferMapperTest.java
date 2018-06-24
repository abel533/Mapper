package tk.mybatis.mapper.additional.update.differ;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;
import tk.mybatis.mapper.additional.Country;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public class UpdateByDifferMapperTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    };


    @Test
    public void testUpdateByDiffer() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country old = mapper.selectByPrimaryKey(1L);
            //(1, 'Angola', 'AO', 1)
            Country newer = new Country();
            newer.setId(1L);
            newer.setCountryname("Newer");
            newer.setCountrycode("AO");
            int count = mapper.updateByDiffer(old, newer);
            Assert.assertEquals(1, count);
            old = mapper.selectByPrimaryKey(1L);
            Assert.assertEquals(1L, old.getId().longValue());
            Assert.assertEquals("Newer", old.getCountryname());
            Assert.assertEquals("AO", old.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

}
