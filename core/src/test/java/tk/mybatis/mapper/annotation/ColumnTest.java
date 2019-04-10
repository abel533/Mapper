package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.StringTypeHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author liuzh
 */
public class ColumnTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserColumn {
        @Column(name = "user_name")
        private String name;
    }

    @Test
    public void testColumn(){
        EntityHelper.initEntityNameMap(UserColumn.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserColumn.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("user_name", column.getColumn());
            Assert.assertEquals("name", column.getProperty());

            Assert.assertEquals("user_name = #{name}", column.getColumnEqualsHolder());
            Assert.assertEquals("user_name = #{record.name}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{name}", column.getColumnHolder());
            Assert.assertEquals("#{record.name}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.name}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.namesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("user_name", resultMapping.getColumn());
        Assert.assertEquals("name", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

}
