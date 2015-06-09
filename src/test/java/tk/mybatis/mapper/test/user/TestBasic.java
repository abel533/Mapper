package tk.mybatis.mapper.test.user;

import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserInfoMapper;
import tk.mybatis.mapper.model.UserInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

/**
 * 测试增删改查
 *
 * @author liuzh
 */
public class TestBasic {


    /**
     * 新增
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername("abel533");
            userInfo.setPassword("123456");
            userInfo.setUsertype("2");
            userInfo.setEmail("abel533@gmail.com");
            Collection collection = sqlSession.getConfiguration().getMappedStatements();
            for (Object o : collection) {
                if(o instanceof MappedStatement){
                    MappedStatement ms = (MappedStatement) o;
                    if (ms.getId().contains("UserInfoMapper.insert")) {
                        System.out.println(ms.getId());
                    }
                }
            }

            Assert.assertEquals(1, mapper.insert(userInfo));

            Assert.assertNotNull(userInfo.getId());
            Assert.assertEquals(6, (int)userInfo.getId());

            Assert.assertEquals(1,mapper.deleteByPrimaryKey(userInfo));
        } finally {
            sqlSession.rollback();
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
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            //查询总数
            Assert.assertEquals(5, mapper.selectCount(new UserInfo()));
            //查询100
            UserInfo userInfo = mapper.selectByPrimaryKey(1);


            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(1));


            //查询总数
            Assert.assertEquals(4, mapper.selectCount(new UserInfo()));
            //插入
            Assert.assertEquals(1, mapper.insert(userInfo));
        } finally {
            sqlSession.rollback();
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
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsertype("1");
            List<UserInfo> userInfos = mapper.select(userInfo);
            Assert.assertEquals(3, userInfos.size());
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
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = mapper.selectByPrimaryKey(2);
            Assert.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setEmail("abel533@gmail.com");
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKey(userInfo));

            userInfo = mapper.selectByPrimaryKey(userInfo);
            Assert.assertNull(userInfo.getUsertype());
            Assert.assertEquals("abel533@gmail.com", userInfo.getEmail());
        } finally {
            sqlSession.rollback();
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
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = mapper.selectByPrimaryKey(1);
            Assert.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setEmail("abel533@gmail.com");
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(userInfo));

            userInfo = mapper.selectByPrimaryKey(1);
            Assert.assertEquals("1", userInfo.getUsertype());
            Assert.assertEquals("abel533@gmail.com", userInfo.getEmail());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


}
