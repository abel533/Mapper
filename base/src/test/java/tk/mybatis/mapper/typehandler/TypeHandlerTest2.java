package tk.mybatis.mapper.typehandler;

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
public class TypeHandlerTest2 extends BaseTest{

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnumAsSimpleType(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(TypeHandlerTest2.class.getResource("mybatis-config-typehandler2.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(TypeHandlerTest2.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testSelect(){
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            List<User2> users = userMapper.selectAll();
            Assert.assertNotNull(users);
            Assert.assertEquals(2, users.size());

            Assert.assertEquals("abel533", users.get(0).getName());
            Assert.assertEquals("Hebei", users.get(0).getAddress().getProvince());
            Assert.assertEquals("Shijiazhuang", users.get(0).getAddress().getCity());
            Assert.assertEquals(StateEnum.enabled, users.get(0).getState());

            Assert.assertEquals("isea533", users.get(1).getName());
            Assert.assertEquals("Hebei/Handan", users.get(1).getAddress().toString());
            Assert.assertEquals(StateEnum.disabled, users.get(1).getState());

            User2 user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals("Hebei", user.getAddress().getProvince());
            Assert.assertEquals("Shijiazhuang", user.getAddress().getCity());
            Assert.assertEquals(StateEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert(){
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);

            User2 user = new User2();
            user.setId(3);
            user.setName("liuzh");
            Address address = new Address();
            address.setProvince("Hebei");
            address.setCity("Qinhuangdao");
            user.setAddress(address);
            user.setState(StateEnum.enabled);

            Assert.assertEquals(1, userMapper.insert(user));

            user = userMapper.selectByPrimaryKey(3);
            Assert.assertEquals("liuzh", user.getName());
            Assert.assertEquals("Hebei", user.getAddress().getProvince());
            Assert.assertEquals("Qinhuangdao", user.getAddress().getCity());
            Assert.assertEquals(StateEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate(){
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            User2 user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals("Hebei", user.getAddress().getProvince());
            Assert.assertEquals("Shijiazhuang", user.getAddress().getCity());
            Assert.assertEquals(StateEnum.enabled, user.getState());

            user.setState(StateEnum.disabled);
            user.getAddress().setCity("Handan");
            Assert.assertEquals(1, userMapper.updateByPrimaryKey(user));

            user = userMapper.selectByPrimaryKey(1);
            Assert.assertEquals("abel533", user.getName());
            Assert.assertEquals("Hebei", user.getAddress().getProvince());
            Assert.assertEquals("Handan", user.getAddress().getCity());
            Assert.assertEquals(StateEnum.disabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete(){
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            Assert.assertEquals(1, userMapper.deleteByPrimaryKey(1));

            User2 user = new User2();
            Address address = new Address();
            address.setProvince("Hebei");
            address.setCity("Handan");
            user.setAddress(address);
            user.setState(StateEnum.enabled);
            Assert.assertEquals(0, userMapper.delete(user));

            user.setState(StateEnum.disabled);
            Assert.assertEquals(1, userMapper.delete(user));
        } finally {
            sqlSession.close();
        }
    }

}
