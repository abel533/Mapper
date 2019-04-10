package tk.mybatis.mapper.annotation;

import org.apache.ibatis.session.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Set;

/**
 * @author liuzh
 */
public class KeySqlTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserJDBC {
        @KeySql(useGeneratedKeys = true)
        private Long id;
    }

    @Test
    public void testUseGeneratedKeys(){
        EntityHelper.initEntityNameMap(UserJDBC.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJDBC.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("JDBC", column.getGenerator());
            Assert.assertTrue(column.isIdentity());
        }
    }

    class UserDialect {
        @KeySql(dialect = IdentityDialect.MYSQL)
        private Long id;
    }

    @Test
    public void testDialect(){
        EntityHelper.initEntityNameMap(UserDialect.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserDialect.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("SELECT LAST_INSERT_ID()", column.getGenerator());
            Assert.assertEquals(ORDER.AFTER, column.getOrder());
            Assert.assertTrue(column.isIdentity());
        }
    }

    class UserSql {
        @KeySql(sql = "select seq.nextval from dual", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testSql(){
        EntityHelper.initEntityNameMap(UserSql.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserSql.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("select seq.nextval from dual", column.getGenerator());
            Assert.assertEquals(ORDER.BEFORE, column.getOrder());
            Assert.assertTrue(column.isIdentity());
        }
    }

    class UserAll {
        @KeySql(useGeneratedKeys = true, dialect = IdentityDialect.MYSQL, sql = "select 1", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testAll(){
        EntityHelper.initEntityNameMap(UserAll.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("JDBC", column.getGenerator());
            Assert.assertTrue(column.isIdentity());
        }
    }

    class UserAll2 {
        @KeySql(dialect = IdentityDialect.MYSQL, sql = "select 1", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testAll2(){
        EntityHelper.initEntityNameMap(UserAll2.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll2.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("SELECT LAST_INSERT_ID()", column.getGenerator());
            Assert.assertEquals(ORDER.AFTER, column.getOrder());
            Assert.assertTrue(column.isIdentity());
        }
    }

}
