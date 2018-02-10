package tk.mybatis.mapper.annotation;

import org.apache.ibatis.session.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.version.VersionException;

import java.util.Set;

/**
 * @author liuzh
 */
public class VersionTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserVersion {
        @Version
        private String name;
    }

    @Test
    public void testVersion(){
        EntityHelper.initEntityNameMap(UserVersion.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserVersion.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertTrue(column.getEntityField().isAnnotationPresent(Version.class));
        }
    }

    /**
     * 一个实体类中只能有一个 @Version 注解
     */
    class UserVersionError {
        @Version
        private Long id;

        @Version
        private String name;
    }

    @Test(expected = VersionException.class)
    public void testVersionError(){
        EntityHelper.initEntityNameMap(UserVersionError.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserVersionError.class);
        Assert.assertNotNull(entityTable);
        SqlHelper.wherePKColumns(UserVersionError.class, true);
    }

}
