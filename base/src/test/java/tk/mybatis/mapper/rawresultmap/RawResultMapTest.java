package tk.mybatis.mapper.rawresultmap;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public class RawResultMapTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setStyle(Style.normal);
        config.setEnableBaseResultMapFlag(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(RawResultMapTest.class.getResource("mybatis-config-rawresultmap.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = RawResultMapTest.class.getResource("CreateDB.sql");
        return toReader(url);
    }

    @Test
    public void testSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            List<User> users;

            System.out.println("------selectAll------");
            users = mapper.selectAll();
            users.forEach(u -> {
                System.out.println(u);
                Assert.assertNotNull(u.getUname());
                Assert.assertNotNull(u.getAge());
                Assert.assertNotNull(u.getCreateTime());
                Assert.assertNull(u.getEmail());
            });
            System.out.println("------------");

            System.out.println("------selectRawAnnotation------");
            users = mapper.selectRawAnnotation();
            users.forEach(u -> {
                System.out.println(u);
                Assert.assertNotNull(u.getUname());
                Assert.assertNotNull(u.getAge());
                Assert.assertNotNull(u.getCreateTime());
                Assert.assertNotNull(u.getEmail());
            });
            System.out.println("------------");

            System.out.println("------fetchRawResultMap------");
            users = mapper.fetchRawResultMap();
            users.forEach(u -> {
                System.out.println(u);
                Assert.assertNotNull(u.getUname());
                Assert.assertNull(u.getAge());
                Assert.assertNotNull(u.getCreateTime());
                Assert.assertNotNull(u.getEmail());
            });
            System.out.println("------------");

            System.out.println("------fetchRawResultType------");
            users = mapper.fetchRawResultType();
            users.forEach(u -> {
                System.out.println(u);
                Assert.assertNotNull(u.getUname());
                Assert.assertNotNull(u.getAge());
                Assert.assertNotNull(u.getCreateTime());
                Assert.assertNotNull(u.getEmail());
            });
            System.out.println("------------");

            System.out.println("------getMapUser------");
            Map<String, Object> mapUser = mapper.getMapUser();
            System.out.println(mapUser);
            System.out.println("------------");

            Integer x = mapper.selectCount2();
            System.out.println(x);
        } finally {
            sqlSession.close();
        }
    }
}
