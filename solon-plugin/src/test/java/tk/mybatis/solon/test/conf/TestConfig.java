package tk.mybatis.solon.test.conf;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @title: TestConfig
 * @author: trifolium.wang
 * @date: 2024/4/2
 */
@Configuration
public class TestConfig {

    Logger log = LoggerFactory.getLogger(TestConfig.class);

    @Bean(name = "db1", typed = true)
    public DataSource db1(@Inject("${test.db1}") HikariDataSource ds) {
        try {
            Connection conn = ds.getConnection();
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE user (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "  `name` varchar(255) DEFAULT NULL," +
                    "  `age` int DEFAULT NULL," +
                    "  `create_time` datetime DEFAULT NULL," +
                    "  `is_del` tinyint(1) DEFAULT NULL" +
                    ")");

            statement.execute("INSERT INTO `user` (`id`, `name`, `age`, `create_time`, `is_del`) VALUES (1, '张三', 11, '2024-04-02 13:38:56', 0);\n" +
                    "INSERT INTO `user` (`id`, `name`, `age`, `create_time`, `is_del`) VALUES (2, '李四', 3, '2024-04-02 13:39:08', 0);\n" +
                    "INSERT INTO `user` (`id`, `name`, `age`, `create_time`, `is_del`) VALUES (3, '张麻子', 43, '2024-04-02 13:39:20', 0);");
            statement.close();
            conn.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Datasource initialization Failure!");
        }
        return ds;
    }
}
