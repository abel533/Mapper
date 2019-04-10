package tk.mybatis.mapper.generatedvalue;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;

/**
 * @author liuzh
 */
@Ignore("这个测试需要使用 MySql 数据库")
public class GeneratedValueTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setOrder("AFTER");
        config.setIDENTITY("MYSQL");
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(GeneratedValueTest.class.getResource("mybatis-config-keysql-mysql.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return null;
    }

    @Test
    public void testUserAutoIncrement() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserAutoIncrementMapper mapper = sqlSession.getMapper(UserAutoIncrementMapper.class);

            UserAutoIncrement user = new UserAutoIncrement();
            user.setName("liuzh");
            Assert.assertEquals(1, mapper.insert(user));
            Assert.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assert.assertEquals("liuzh", user.getName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUserAutoIncrementIdentity() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserAutoIncrementIdentityMapper mapper = sqlSession.getMapper(UserAutoIncrementIdentityMapper.class);

            UserAutoIncrementIdentity user = new UserAutoIncrementIdentity();
            user.setName("liuzh");
            Assert.assertEquals(1, mapper.insert(user));
            Assert.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assert.assertEquals("liuzh", user.getName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUserSqlAfter() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserSqlAfterMapper mapper = sqlSession.getMapper(UserSqlAfterMapper.class);

            UserSqlAfter user = new UserSqlAfter();
            user.setName("liuzh");
            Assert.assertEquals(1, mapper.insert(user));
            Assert.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assert.assertEquals("liuzh", user.getName());
        } finally {
            sqlSession.close();
        }
    }

}
