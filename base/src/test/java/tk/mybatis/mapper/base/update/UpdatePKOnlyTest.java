package tk.mybatis.mapper.base.update;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;

public class UpdatePKOnlyTest extends BaseTest {

    /**
     * 如果更新的所有属性（除ID外）都是 null，在3.x版本以前会语法错误，4.0 以后，sql 会变成如 update table set id = id where id = ?
     */
    @Test
    public void testUpdatePKOnly(){
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setId(1L);
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(country));
            country.setId(9999L);
            Assert.assertEquals(0, mapper.updateByPrimaryKeySelective(country));
        } finally {
            sqlSession.close();
        }
    }

}
