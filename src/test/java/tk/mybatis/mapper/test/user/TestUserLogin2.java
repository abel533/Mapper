package tk.mybatis.mapper.test.user;

import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserLogin2Mapper;
import tk.mybatis.mapper.model.UserLogin2;
import tk.mybatis.mapper.model.UserLogin2Key;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * 通过主键删除
 *
 * @author liuzh
 */
public class TestUserLogin2 {


    /**
     * 新增
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);

            UserLogin2 userLogin = new UserLogin2();
            userLogin.setUsername("abel533");
            userLogin.setLogindate(new Date());
            userLogin.setLoginip("192.168.123.1");

            Assert.assertEquals(1, mapper.insert(userLogin));

            Assert.assertNotNull(userLogin.getLogid());
            Assert.assertTrue(userLogin.getLogid() > 10);
            //这里测了实体类入参的删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(userLogin));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 主要测试删除
     */
    @Test
    public void testDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            //查询总数
            Assert.assertEquals(10, mapper.selectCount(new UserLogin2()));
            //根据主键查询
            UserLogin2Key key = new UserLogin2();
            key.setLogid(1);
            key.setUsername("test1");
            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(key));

            //查询总数
            Assert.assertEquals(9, mapper.selectCount(new UserLogin2()));
            //插入
            Assert.assertEquals(1, mapper.insert(userLogin));
        } finally {
            sqlSession.close();
        }
    }


    /**
     * 查询
     */
    @Test
    public void testSelect() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            UserLogin2 userLogin = new UserLogin2();
            userLogin.setUsername("test1");
            List<UserLogin2> userLogins = mapper.select(userLogin);
            Assert.assertEquals(5, userLogins.size());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据主键全更新
     */
    @Test
    public void testUpdateByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            UserLogin2Key key = new UserLogin2();
            key.setLogid(2);
            key.setUsername("test1");
            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            Assert.assertNotNull(userLogin);
            userLogin.setLoginip("1.1.1.1");
            userLogin.setLogindate(null);
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKey(userLogin));

            userLogin = mapper.selectByPrimaryKey(userLogin);
            Assert.assertNull(userLogin.getLogindate());
            Assert.assertEquals("1.1.1.1", userLogin.getLoginip());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据主键更新非null
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);

            UserLogin2Key key = new UserLogin2();
            key.setLogid(1);
            key.setUsername("test1");

            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            Assert.assertNotNull(userLogin);
            userLogin.setLogindate(null);
            userLogin.setLoginip("1.1.1.1");
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(userLogin));

            userLogin = mapper.selectByPrimaryKey(key);
            Assert.assertNotNull(userLogin.getLogindate());
            Assert.assertEquals("1.1.1.1", userLogin.getLoginip());
        } finally {
            sqlSession.close();
        }
    }


}
