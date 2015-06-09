package tk.mybatis.mapper.test.user;

import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserInfoMapMapper;
import tk.mybatis.mapper.model.UserInfoMap;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;

import java.util.List;

/**
 * 测试增删改查 - 不在直接支持Map
 *
 * @author liuzh
 */
public class TestMap {


    /**
     * 新增
     */
//    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = new UserInfoMap();
            userInfoMap.setUserName("abel533");
            userInfoMap.setPassword("123456");
            userInfoMap.setUserType("2");

            Assert.assertEquals(1, mapper.insert(userInfoMap));

            Assert.assertNotNull(userInfoMap.getId());
            Assert.assertEquals(6, (int)userInfoMap.getId());

            Assert.assertEquals(1,mapper.deleteByPrimaryKey(userInfoMap));
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 主要测试删除
     */
//    @Test
    public void testDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            //查询总数
            Assert.assertEquals(5, mapper.selectCount(new UserInfoMap()));
            //查询100
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(1);

            //根据主键删除
            Assert.assertEquals(1, mapper.deleteByPrimaryKey(1));

            //查询总数
            Assert.assertEquals(4, mapper.selectCount(new UserInfoMap()));
            //插入
            Assert.assertEquals(1, mapper.insert(userInfoMap));
        } finally {
            sqlSession.close();
        }
    }


    /**
     * 查询
     */
//    @Test
    public void testSelect() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = new UserInfoMap();
            userInfoMap.setUserType("1");
            List<UserInfoMap> UserInfoMaps = mapper.select(userInfoMap);
            Assert.assertEquals(3, UserInfoMaps.size());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据主键全更新
     */
//    @Test
    public void testUpdateByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(2);
            Assert.assertNotNull(userInfoMap);
            userInfoMap.setUserType(null);
            userInfoMap.setRealName("liuzh");
            //不会更新user_type
            Assert.assertEquals(1, mapper.updateByPrimaryKey(userInfoMap));

            userInfoMap = mapper.selectByPrimaryKey(userInfoMap);
            Assert.assertNull(userInfoMap.getUserType());
            Assert.assertEquals("liuzh",userInfoMap.getRealName());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据主键更新非null
     */
//    @Test
    public void testUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(1);
            Assert.assertNotNull(userInfoMap);
            userInfoMap.setUserType(null);
            userInfoMap.setRealName("liuzh");
            //不会更新user_type
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(userInfoMap));

            userInfoMap = mapper.selectByPrimaryKey(1);
            Assert.assertEquals("1", userInfoMap.getUserType());
            Assert.assertEquals("liuzh",userInfoMap.getRealName());
        } finally {
            sqlSession.close();
        }
    }


}
