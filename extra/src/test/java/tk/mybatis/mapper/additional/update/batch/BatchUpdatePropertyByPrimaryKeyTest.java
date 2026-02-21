package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;
import tk.mybatis.mapper.additional.Country;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author goldhuang
 * @Description: 验证批量更新
 * @date 2020-04-02
 */
public class BatchUpdatePropertyByPrimaryKeyTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    ;

    @Test
    public void testBatchUpdateFieldByIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            tk.mybatis.mapper.additional.update.batch.CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            //(1, 'Angola', 'AO', 1)
            Country old1 = mapper.selectByPrimaryKey(1L);
            //(2, 'Afghanistan', 'AF', 1)
            Country old2 = mapper.selectByPrimaryKey(2L);

            int updateCount = mapper.batchUpdateFieldByIdList(Country::getCountryname, "Gold", Arrays.asList(old1.getId(), old2.getId()));
            Assert.assertEquals(2, updateCount);

            old1 = mapper.selectByPrimaryKey(1L);
            old2 = mapper.selectByPrimaryKey(2L);

            Assert.assertEquals(1L, old1.getId().longValue());
            Assert.assertEquals(2L, old2.getId().longValue());
            Assert.assertEquals("Gold", old1.getCountryname());
            Assert.assertEquals("Gold", old2.getCountryname());
        } finally {
            sqlSession.close();
        }
    }

    @Test(expected = Exception.class)
    public void testBatchUpdateFieldByEmptyIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            tk.mybatis.mapper.additional.update.batch.CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            mapper.batchUpdateFieldByIdList(Country::getCountryname, "Gold", new ArrayList<>());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void batchUpdateFieldListByIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            tk.mybatis.mapper.additional.update.batch.CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

            //(1, 'Angola', 'AO', 1)
            Country old1 = mapper.selectByPrimaryKey(1L);
            //(2, 'Afghanistan', 'AF', 1)
            Country old2 = mapper.selectByPrimaryKey(2L);

            List<FieldValue<Country>> fieldValues = new ArrayList<>();
            fieldValues.add(new FieldValue<>(Country::getCountrycode, "GD"));
            fieldValues.add(new FieldValue<>(Country::getCountryname, "Gold"));

            int updateCount = mapper.batchUpdateFieldListByIdList(fieldValues, Arrays.asList(old1.getId(), old2.getId()));
            Assert.assertEquals(2, updateCount);

            old1 = mapper.selectByPrimaryKey(1L);
            old2 = mapper.selectByPrimaryKey(2L);

            Assert.assertEquals(1L, old1.getId().longValue());
            Assert.assertEquals(2L, old2.getId().longValue());
            Assert.assertEquals("Gold", old1.getCountryname());
            Assert.assertEquals("Gold", old2.getCountryname());
            Assert.assertEquals("GD", old1.getCountrycode());
            Assert.assertEquals("GD", old2.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

}
