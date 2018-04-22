package tk.mybatis.mapper.base.genid;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * @author liuzh
 */
public class InsertGenIdTest extends BaseTest {
    private String[][] countries = new String[][]{
            {"Angola", "AO"},
            {"Afghanistan", "AF"},
            {"Albania", "AL"},
            {"Algeria", "DZ"},
            {"Andorra", "AD"},
            {"Anguilla", "AI"},
            {"Antigua and Barbuda", "AG"},
            {"Argentina", "AR"},
            {"Armenia", "AM"},
            {"Australia", "AU"},
            {"Austria", "AT"},
            {"Azerbaijan", "AZ"},
            {"Bahamas", "BS"},
            {"Bahrain", "BH"},
            {"Bangladesh", "BD"},
            {"Barbados", "BB"},
            {"Belarus", "BY"},
            {"Belgium", "BE"},
            {"Belize", "BZ"},
            {"Benin", "BJ"},
            {"Bermuda Is.", "BM"},
            {"Bolivia", "BO"},
            {"Botswana", "BW"},
            {"Brazil", "BR"},
            {"Brunei", "BN"},
            {"Bulgaria", "BG"},
            {"Burkina-faso", "BF"},
            {"Burma", "MM"},
            {"Burundi", "BI"},
            {"Cameroon", "CM"},
            {"Canada", "CA"},
            {"Central African Republic", "CF"},
            {"Chad", "TD"},
            {"Chile", "CL"},
            {"China", "CN"}
    };

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    };

    /**
     * 获取初始化 sql
     *
     * @return
     */
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = getClass().getResource("CreateDB.sql");
        return toReader(url);
    };

    @Test
    public void testGenId(){
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            for (int i = 0; i < countries.length; i++) {
                Country country = new Country(countries[i][0], countries[i][1]);
                Assert.assertEquals(1, mapper.insert(country));
                Assert.assertNotNull(country.getId());
                System.out.println(country.getId());
            }
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testUUID(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            for (int i = 0; i < countries.length; i++) {
                User user = new User(countries[i][0], countries[i][1]);
                Assert.assertEquals(1, mapper.insert(user));
                Assert.assertNotNull(user.getId());
                System.out.println(user.getId());
            }
        } finally {
            sqlSession.close();
        }
    }

}
