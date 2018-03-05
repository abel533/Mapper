/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package tk.mybatis.mapper.weekend;

import org.apache.ibatis.session.SqlSession;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import tk.mybatis.mapper.weekend.entity.User;
import tk.mybatis.mapper.weekend.mapper.UserMapper;

import java.util.Arrays;
import java.util.List;

/**
 * @author Frank
 */
public class UserMapperTest {

    @Rule
    public TestRule runJava8 = new UseJava8Rule();

    /**
     * 执行，然后看日志打出来的SQL
     */
    @Test
    public void testSelectIdIsNull(){
        SqlSession    sqlSession = MybatisHelper.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Weekend<User> weekend = Weekend.of(User.class);
        weekend.weekendCriteria()
                .andIsNull(User::getId)
                .andBetween(User::getId,0,10)
                .andIn(User::getUserName, Arrays.asList("a","b","c"));

        List<User> users = userMapper.selectByExample(weekend);
        for (User user : users) {
            System.out.println(user.getUserName());
        }
    }
}
