/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
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

package com.quancheng.mybatis.mapper.mapper;

import com.quancheng.mybatis.mapper.common.IdsMapper;
import com.quancheng.mybatis.mapper.common.Mapper;
import com.quancheng.mybatis.mapper.common.SqlServerMapper;
import com.quancheng.mybatis.mapper.entity.Config;
import com.quancheng.mybatis.mapper.hsqldb.HsqldbMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import com.quancheng.mybatis.mapper.common.MySqlMapper;
import com.quancheng.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

/**
 * Description: MybatisHelper
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:33)
 */
public class MybatisHelper {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //创建SqlSessionFactory
            Reader reader = Resources.getResourceAsReader("mybatis-java.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
            //创建数据库
            SqlSession session = null;
            try {
                session = sqlSessionFactory.openSession();
                //创建一个MapperHelper
                MapperHelper mapperHelper = new MapperHelper();
                //特殊配置
                Config config = new Config();
                // 设置UUID生成策略
                // 配置UUID生成策略需要使用OGNL表达式
                // 默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
                //config.setUUID("");
                // 主键自增回写方法,默认值MYSQL,详细说明请看文档
                config.setIDENTITY("HSQLDB");
                // 支持方法上的注解
                // 3.3.1版本增加
                config.setEnableMethodAnnotation(true);
                config.setNotEmpty(true);
                // 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
                // 可选参数一共3个，对应0,1,2,分别为SequenceName，ColumnName, PropertyName
                //config.setSeqFormat("NEXT VALUE FOR {0}");
                // 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
                //config.setCatalog("");
                // 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
                // 如果同时设置了catalog,优先使用catalog.tablename
                //config.setSchema("");
                // 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
                //config.setOrder("AFTER");
                //设置配置
                mapperHelper.setConfig(config);
                // 注册通用Mapper接口 - 可以自动注册继承的接口
                mapperHelper.registerMapper(Mapper.class);
                mapperHelper.registerMapper(HsqldbMapper.class);
                mapperHelper.registerMapper(MySqlMapper.class);
                mapperHelper.registerMapper(SqlServerMapper.class);
                mapperHelper.registerMapper(IdsMapper.class);
                //配置完成后，执行下面的操作
                mapperHelper.processConfiguration(session.getConfiguration());
                //OK - mapperHelper的任务已经完成，可以不管了

                Connection conn = session.getConnection();
                reader = Resources.getResourceAsReader("CreateDB.sql");
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
     * 获取Session
     * @return
     */
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
}
