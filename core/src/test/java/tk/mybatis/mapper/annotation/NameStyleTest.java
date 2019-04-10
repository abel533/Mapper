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

import java.util.Set;

/**
 * @author liuzh
 */
public class NameStyleTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    @NameStyle(Style.camelhump)
    class UserCamelhump {
        private String userName;
    }

    @Test
    public void testCamelhump(){
        EntityHelper.initEntityNameMap(UserCamelhump.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhump.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("user_camelhump", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("user_name", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("user_name = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("user_name = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("user_name", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.camelhumpAndUppercase)
    class UserCamelhumpAndUppercase {
        private String userName;
    }

    @Test
    public void testCamelhumpAndUppercase(){
        EntityHelper.initEntityNameMap(UserCamelhumpAndUppercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhumpAndUppercase.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("USER_CAMELHUMP_AND_UPPERCASE", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("USER_NAME", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("USER_NAME = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("USER_NAME = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("USER_NAME", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.camelhumpAndLowercase)
    class UserCamelhumpAndLowercase {
        private String userName;
    }

    @Test
    public void testCamelhumpAndLowercase(){
        EntityHelper.initEntityNameMap(UserCamelhumpAndLowercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhumpAndLowercase.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("user_camelhump_and_lowercase", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("user_name", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("user_name = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("user_name = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("user_name", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.normal)
    class UserNormal {
        private String userName;
    }

    @Test
    public void testNormal(){
        EntityHelper.initEntityNameMap(UserNormal.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserNormal.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("UserNormal", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("userName", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("userName = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("userName = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("userName", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.uppercase)
    class UserUppercase {
        private String userName;
    }

    @Test
    public void testUppercase(){
        EntityHelper.initEntityNameMap(UserUppercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserUppercase.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("USERUPPERCASE", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("USERNAME", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("USERNAME = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("USERNAME = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("USERNAME", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.lowercase)
    class UserLowercase {
        private String userName;
    }

    @Test
    public void testLowercase(){
        EntityHelper.initEntityNameMap(UserLowercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserLowercase.class);
        Assert.assertNotNull(entityTable);
        Assert.assertEquals("userlowercase", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("username", column.getColumn());
            Assert.assertEquals("userName", column.getProperty());

            Assert.assertEquals("username = #{userName}", column.getColumnEqualsHolder());
            Assert.assertEquals("username = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{userName}", column.getColumnHolder());
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("username", resultMapping.getColumn());
        Assert.assertEquals("userName", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

}
