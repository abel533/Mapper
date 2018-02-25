package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
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
public class ColumnTypeTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserColumn {
        @ColumnType(column = "user_name")
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

    class UserJdbcTypeVarchar {
        @ColumnType(jdbcType = JdbcType.VARCHAR)
        private String name;
    }

    @Test
    public void testJdbcTypeVarchar(){
        EntityHelper.initEntityNameMap(UserJdbcTypeVarchar.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJdbcTypeVarchar.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("name", column.getColumn());
            Assert.assertEquals("name", column.getProperty());

            Assert.assertEquals("name = #{name, jdbcType=VARCHAR}", column.getColumnEqualsHolder());
            Assert.assertEquals("name = #{record.name, jdbcType=VARCHAR}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{name, jdbcType=VARCHAR}", column.getColumnHolder());
            Assert.assertEquals("#{record.name, jdbcType=VARCHAR}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.name, jdbcType=VARCHAR}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.namesuffix, jdbcType=VARCHAR},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("name", resultMapping.getColumn());
        Assert.assertEquals("name", resultMapping.getProperty());
        Assert.assertNotNull(resultMapping.getJdbcType());
        Assert.assertEquals(JdbcType.VARCHAR, resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserJdbcTypeBlob {
        @ColumnType(jdbcType = JdbcType.BLOB)
        private String name;
    }

    @Test
    public void testJdbcTypeBlob(){
        EntityHelper.initEntityNameMap(UserJdbcTypeBlob.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJdbcTypeBlob.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("name", column.getColumn());
            Assert.assertEquals("name", column.getProperty());

            Assert.assertEquals("name = #{name, jdbcType=BLOB}", column.getColumnEqualsHolder());
            Assert.assertEquals("name = #{record.name, jdbcType=BLOB}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{name, jdbcType=BLOB}", column.getColumnHolder());
            Assert.assertEquals("#{record.name, jdbcType=BLOB}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.name, jdbcType=BLOB}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.namesuffix, jdbcType=BLOB},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("name", resultMapping.getColumn());
        Assert.assertEquals("name", resultMapping.getProperty());
        Assert.assertNotNull(resultMapping.getJdbcType());
        Assert.assertEquals(JdbcType.BLOB, resultMapping.getJdbcType());
        Assert.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserTypehandler {
        @ColumnType(typeHandler = BlobTypeHandler.class)
        private String name;
    }

    @Test
    public void testTypehandler(){
        EntityHelper.initEntityNameMap(UserTypehandler.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserTypehandler.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("name", column.getColumn());
            Assert.assertEquals("name", column.getProperty());

            Assert.assertEquals("name = #{name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder());
            Assert.assertEquals("name = #{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder());
            Assert.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.namesuffix, typeHandler=org.apache.ibatis.type.BlobTypeHandler},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNotNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("name", resultMapping.getColumn());
        Assert.assertEquals("name", resultMapping.getProperty());
        Assert.assertNull(resultMapping.getJdbcType());
        Assert.assertEquals(BlobTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserAll {
        @ColumnType(column = "user_name", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
        private String name;
    }

    @Test
    public void testAll(){
        EntityHelper.initEntityNameMap(UserAll.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertEquals("user_name", column.getColumn());
            Assert.assertEquals("name", column.getProperty());

            Assert.assertEquals("user_name = #{name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder());
            Assert.assertEquals("user_name = #{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder("record"));
            Assert.assertEquals("#{name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder());
            Assert.assertEquals("#{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record"));
            Assert.assertEquals("#{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record", "suffix"));
            Assert.assertEquals("#{record.namesuffix, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler},", column.getColumnHolder("record", "suffix", ","));
            Assert.assertNotNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assert.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assert.assertEquals("user_name", resultMapping.getColumn());
        Assert.assertEquals("name", resultMapping.getProperty());
        Assert.assertNotNull(resultMapping.getJdbcType());
        Assert.assertEquals(BlobTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

}
