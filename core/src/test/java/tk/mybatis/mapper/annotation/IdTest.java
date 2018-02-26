package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
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

import javax.persistence.Id;
import java.util.Set;

/**
 * @author liuzh
 */
public class IdTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest(){
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserSingleId {
        @Id
        private String name;
    }

    @Test
    public void testSingleId(){
        EntityHelper.initEntityNameMap(UserSingleId.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserSingleId.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assert.assertTrue(column.isId());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals(1, resultMap.getResultMappings().size());
        Assert.assertTrue(resultMap.getResultMappings().get(0).getFlags().contains(ResultFlag.ID));

        Assert.assertEquals("<where> AND name = #{name}</where>", SqlHelper.wherePKColumns(UserSingleId.class));
    }

    class UserCompositeKeys  {
        @Id
        private String name;

        @Id
        private String orgId;
    }

    @Test
    public void testCompositeKeys(){
        EntityHelper.initEntityNameMap(UserCompositeKeys.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCompositeKeys.class);
        Assert.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assert.assertEquals(2, columns.size());
        Assert.assertEquals(2, entityTable.getEntityClassPKColumns().size());

        for (EntityColumn column : columns) {
            Assert.assertTrue(column.isId());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assert.assertEquals(2, resultMap.getResultMappings().size());
        Assert.assertTrue(resultMap.getResultMappings().get(0).getFlags().contains(ResultFlag.ID));
        Assert.assertTrue(resultMap.getResultMappings().get(1).getFlags().contains(ResultFlag.ID));

        Assert.assertEquals("<where> AND name = #{name} AND orgId = #{orgId}</where>", SqlHelper.wherePKColumns(UserCompositeKeys.class));
    }

}
