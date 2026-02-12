package tk.mybatis.mapper.additional.aggregation;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class AggregationHumpTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    /**
     * 获取初始化 sql
     *
     * @return
     */
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = getClass().getResource("CreateDB.sql");
        return toReader(url);
    }

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        // 开启驼峰转下划线，模拟真实环境中可能出现的问题
        config.setStyle(Style.camelhump);
        return config;
    }

    @Test
    public void testAggregationWithCamelHump() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserHumpMapper mapper = sqlSession.getMapper(UserHumpMapper.class);
            AggregateCondition aggregateCondition = AggregateCondition.builder()
                    .aggregateBy("id") // id -> id
                    .aliasName("total")
                    .aggregateType(AggregateType.COUNT)
                    .groupBy("userRole"); // userRole -> user_role

            Example example = new Example(UserHump.class);
            List<UserHump> list = mapper.selectAggregationByExample(example, aggregateCondition);

            // 应该有 2 组: Admin 和 USER
            Assert.assertEquals(2, list.size());

            // 验证 userRole 是否有值
            // 如果 AggregationProvider 依然使用 alias AS user_role (columnName), 
            // 在 mapUnderscoreToCamelCase=false (default) 的情况下，UserHump.userRole 属性将为 null。
            // 只有当 AggregationProvider 使用 alias AS userRole (propertyName) 时，MyBatis 才能自动映射。
            for (UserHump user : list) {
                Assert.assertNotNull("userRole should not be null", user.getUserRole());
                Assert.assertTrue(
                        "userRole should be Admin or USER",
                        "Admin".equals(user.getUserRole()) || "USER".equals(user.getUserRole())
                );
            }


            // 所以，如果我要断言聚合值，我应该把 aliasName 设置为 "aggregation"。

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAggregationValueWithCamelHump() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserHumpMapper mapper = sqlSession.getMapper(UserHumpMapper.class);
            AggregateCondition aggregateCondition = AggregateCondition.builder()
                    .aggregateBy("id")
                    .aliasName("aggregation")
                    .aggregateType(AggregateType.COUNT)
                    .groupBy("userRole");

            Example example = new Example(UserHump.class);
            example.setOrderByClause("user_role asc"); // 数据库列名
            List<UserHump> list = mapper.selectAggregationByExample(example, aggregateCondition);

            Assert.assertEquals(2, list.size());
            // Admin 有 3 个 (1,2,3)
            // USER 有 3 个 (4,5,6)
            for (UserHump u : list) {
                Assert.assertEquals(new Long(3), u.getAggregation());
                Assert.assertNotNull(u.getUserRole());
            }
        } finally {
            sqlSession.close();
        }
    }
}
