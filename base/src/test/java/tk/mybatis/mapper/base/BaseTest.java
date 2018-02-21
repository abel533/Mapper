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

package tk.mybatis.mapper.base;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

/**
 * 测试基类
 *
 * @author liuzh
 */
public abstract class BaseTest {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public final void init(){
        try {
            Reader reader = Resources.getResourceAsReader(getConfigPath());
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
            SqlSession session = null;
            try {
                session = sqlSessionFactory.openSession();
                //创建一个MapperHelper
                MapperHelper mapperHelper = new MapperHelper();

                //设置配置
                mapperHelper.setConfig(getConfig());
                //配置完成后，执行下面的操作
                mapperHelper.processConfiguration(session.getConfiguration());
                //OK - mapperHelper的任务已经完成，可以不管了

                Connection conn = session.getConnection();
                reader = Resources.getResourceAsReader(getSqlPath());
                ScriptRunner runner = new ScriptRunner(conn);
                runner.setLogWriter(null);
                runner.runScript(reader);
                reader.close();
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置
     *
     * @return
     */
    protected Config getConfig(){
        return new Config();
    }

    /**
     * 获取 mybatis 配置路径
     *
     * @return
     */
    protected String getConfigPath(){
        final String path = BaseTest.class.getResource("mybatis-config.xml").getPath();
        File file = new File(path);
        System.out.println(file.exists());
        return path;
    };

    /**
     * 获取初始化 sql 路径
     *
     * @return
     */
    protected String getSqlPath(){
        return BaseTest.class.getResource("CreateDB.sql").getPath();
    };

    /**
     * 获取Session
     *
     * @return
     */
    protected SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
