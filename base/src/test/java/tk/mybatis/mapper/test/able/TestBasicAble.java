/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
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
 */

package tk.mybatis.mapper.test.able;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserInfoAbleMapper;
import tk.mybatis.mapper.model.UserInfoAble;

/**
 * 测试增删改查
 *
 * @author liuzh
 */
public class TestBasicAble {

    /**
     * 新增
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoAbleMapper mapper = sqlSession.getMapper(UserInfoAbleMapper.class);
            UserInfoAble userInfo = new UserInfoAble();
            userInfo.setUsername("abel533");
            userInfo.setPassword("123456");
            userInfo.setUsertype("2");
            userInfo.setEmail("abel533@gmail.com");//insert=false

            Assert.assertEquals(1, mapper.insert(userInfo));

            Assert.assertNotNull(userInfo.getId());
            Assert.assertEquals(6, (int) userInfo.getId());

            userInfo = mapper.selectByPrimaryKey(userInfo.getId());
            //email没有插入
            Assert.assertNull(userInfo.getEmail());
        } finally {
            sqlSession.rollback();
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
            UserInfoAbleMapper mapper = sqlSession.getMapper(UserInfoAbleMapper.class);
            UserInfoAble userInfo = mapper.selectByPrimaryKey(2);
            Assert.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setEmail("abel533@gmail.com");
            userInfo.setAddress("这个地址不会更新进去");//update=false
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKey(userInfo));

            userInfo = mapper.selectByPrimaryKey(userInfo);
            Assert.assertNull(userInfo.getUsertype());
            Assert.assertNotEquals("这个地址不会更新进去", userInfo.getAddress());
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
            UserInfoAbleMapper mapper = sqlSession.getMapper(UserInfoAbleMapper.class);
            UserInfoAble userInfo = mapper.selectByPrimaryKey(1);
            Assert.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setPassword(null);
            userInfo.setAddress("这个地址不会更新进去");
            //不会更新username
            Assert.assertEquals(1, mapper.updateByPrimaryKeySelective(userInfo));

            userInfo = mapper.selectByPrimaryKey(1);
            Assert.assertEquals("1", userInfo.getUsertype());
            Assert.assertEquals("12345678", userInfo.getPassword());
            Assert.assertNotEquals("这个地址不会更新进去", userInfo.getAddress());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


}
