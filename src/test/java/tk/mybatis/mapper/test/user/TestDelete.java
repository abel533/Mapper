package tk.mybatis.mapper.test.user;

import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserLoginMapper;
import tk.mybatis.mapper.model.UserLogin;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 通过主键删除
 *
 * @author liuzh
 */
public class TestDelete {

    /**
     * 主要测试删除
     */
    @Test
    public void testDynamicDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserLoginMapper mapper = sqlSession.getMapper(UserLoginMapper.class);
            //查询总数
            Assert.assertEquals(10, mapper.selectCount(new UserLogin()));
            UserLogin userLogin = new UserLogin();
            userLogin.setUsername("test1");
            List<UserLogin> userLoginList = mapper.select(userLogin);
            //批量删除
            Assert.assertEquals(userLoginList.size(), mapper.delete(userLogin));
            //循环插入
            for (int i = 0; i < userLoginList.size(); i++) {
                Assert.assertEquals(1, mapper.insert(userLoginList.get(i)));
                Assert.assertEquals(i + 1, (int) userLoginList.get(i).getLogid());
            }
            //查询总数
            Assert.assertEquals(10, mapper.selectCount(new UserLogin()));
        } finally {
            sqlSession.close();
        }
    }
}
