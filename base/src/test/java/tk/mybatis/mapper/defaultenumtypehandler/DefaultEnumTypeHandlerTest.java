package tk.mybatis.mapper.defaultenumtypehandler;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @author liuzh
 */
public class DefaultEnumTypeHandlerTest extends BaseTest{

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnumAsSimpleType(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(DefaultEnumTypeHandlerTest.class.getResource("mybatis-config-defaultenumtypehandler.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(DefaultEnumTypeHandlerTest.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testSelect(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = userMapper.selectAll();
            Assert.assertNotNull(users);
            Assert.assertEquals(2, users.size());

            Assert.assertEquals("abel533", users.get(0).getName());
            Assert.assertEquals(LockDictEnum.unlocked, users.get(0).getLock());
            Assert.assertEquals(StateDictEnum.enabled, users.get(0).getState());

            Assert.assertEquals("isea533", users.get(1).getName());
            Assert.assertEquals(LockDictEnum.locked, users.get(1).getLock());
            Assert.assertEquals(StateDictEnum.disabled, users.get(1).getState());

            User user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals(LockDictEnum.unlocked, users.get(0).getLock());
            Assert.assertEquals(StateDictEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setId(3);
            user.setName("liuzh");
            user.setLock(LockDictEnum.unlocked);
            user.setState(StateDictEnum.enabled);

            Assert.assertEquals(1, userMapper.insert(user));

            user = userMapper.selectByPrimaryKey(3);
            Assert.assertEquals("liuzh", user.getName());
            Assert.assertEquals(LockDictEnum.unlocked, user.getLock());
            Assert.assertEquals(StateDictEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals(LockDictEnum.unlocked, user.getLock());
            Assert.assertEquals(StateDictEnum.enabled, user.getState());

            user.setLock(LockDictEnum.locked);
            user.setState(StateDictEnum.disabled);
            Assert.assertEquals(1, userMapper.updateByPrimaryKey(user));

            user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals(LockDictEnum.locked, user.getLock());
            Assert.assertEquals(StateDictEnum.disabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Assert.assertEquals(1, userMapper.deleteByPrimaryKey(1));

            User user = new User();
            user.setState(StateDictEnum.enabled);
            Assert.assertEquals(0, userMapper.delete(user));

            user = new User();
            user.setLock(LockDictEnum.unlocked);
            Assert.assertEquals(0, userMapper.delete(user));

            user = new User();
            user.setLock(LockDictEnum.locked);
            user.setState(StateDictEnum.disabled);
            Assert.assertEquals(1, userMapper.delete(user));
        } finally {
            sqlSession.close();
        }
    }

}
