package tk.mybatis.mapper.mapper;

import tk.mybatis.mapper.hsqldb.HsqldbMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.SqlServerMapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

/**
 * Description: MybatisHelper
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:33)
 */
public class MybatisJavaHelper {

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
                // 设置UUID生成策略
                // 配置UUID生成策略需要使用OGNL表达式
                // 默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
                mapperHelper.setUUID("");
                // 主键自增回写方法,默认值MYSQL,详细说明请看文档
                mapperHelper.setIDENTITY("HSQLDB");
                // 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
                // 可选参数一共3个，对应0,1,2,分别为SequenceName，ColumnName, PropertyName
                mapperHelper.setSeqFormat("NEXT VALUE FOR {0}");
                // 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
                mapperHelper.setCatalog("");
                // 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
                // 如果同时设置了catalog,优先使用catalog.tablename
                mapperHelper.setSchema("");
                // 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
                mapperHelper.setOrder("AFTER");
                // 注册通用Mapper接口 - 可以自动注册继承的接口
                mapperHelper.registerMapper(Mapper.class);
                mapperHelper.registerMapper(HsqldbMapper.class);
                mapperHelper.registerMapper(MySqlMapper.class);
                mapperHelper.registerMapper(SqlServerMapper.class);
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
