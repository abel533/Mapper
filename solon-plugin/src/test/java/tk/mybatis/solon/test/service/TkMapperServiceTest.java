package tk.mybatis.solon.test.service;

import org.apache.ibatis.solon.annotation.Db;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.noear.solon.test.annotation.Rollback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.solon.test.TkMapperTest;
import tk.mybatis.solon.test.entity.User;
import tk.mybatis.solon.test.mapper.UserMapper;

import java.util.List;

/**
 * @title: TkMapperServiceTest
 * @author: trifolium.wang
 * @date: 2024/4/2
 */
//@Ignore
@SolonTest(TkMapperTest.class)
@RunWith(SolonJUnit4ClassRunner.class)
public class TkMapperServiceTest {

    Logger log = LoggerFactory.getLogger(TkMapperServiceTest.class);

    @Db("db1")
    private UserMapper userMapper;

    @Test
    public void all() {

        userMapper.selectAll().forEach(u -> log.info(u.toString()));
    }

    /**
     * 根据主键查询
     */
    @Test
    public void byId() {

        User user = userMapper.selectByPrimaryKey(1);
        log.info(user == null ? null : user.toString());
    }

    /**
     * 根据example查询
     */
    @Test
    public void exampleQuery() {
        Example example = new Example(User.class);
        example.and().andLike("name", "%张%");
        userMapper.selectByExample(example).forEach(u -> log.info(u.toString()));
    }

    /**
     * mybatis 原生查询
     */
    @Test
    public void rawMybatisQuery() {

        userMapper.findByGTAge(11).forEach(u -> log.info(u.toString()));
    }

    /**
     * mybatis 逻辑删除和添加,并测试事务
     */
    @Test
    @Rollback
    public void logicDelInsert() {

        List<User> users = userMapper.findByName("张麻子");
        if (!users.isEmpty()) {
            User user = users.get(0);
            userMapper.deleteByPrimaryKey(user.getId());
            user.setId(null);
            userMapper.insert(user);
        }
    }

}
