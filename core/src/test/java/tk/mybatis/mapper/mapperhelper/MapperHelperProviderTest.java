package tk.mybatis.mapper.mapperhelper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.MappedStatement;
import org.junit.Assert;
import org.junit.Test;

public class MapperHelperProviderTest {

    public static class SelectTemplate extends MapperTemplate {
        public SelectTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        public String selectOne(MappedStatement ms) {
            return "";
        }
    }

    public static class InsertTemplate extends MapperTemplate {
        public InsertTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        public String insert(MappedStatement ms) {
            return "";
        }
    }

    public interface MixedMapper {
        @SelectProvider(type = SelectTemplate.class, method = "dynamicSQL")
        Object selectOne(Object record);

        @InsertProvider(type = InsertTemplate.class, method = "dynamicSQL")
        int insert(Object record);

        //用户自定义方法，不属于 provider
        Object customMethod(String name);
    }

    @Test
    public void testCustomMethodAndMultipleProviders() {
        MapperHelper helper = new MapperHelper();
        helper.registerMapper(MixedMapper.class);
        String prefix = MixedMapper.class.getName() + ".";
        Assert.assertTrue(helper.getMapperTemplateByMsId(prefix + "selectOne") instanceof SelectTemplate);
        Assert.assertTrue(helper.getMapperTemplateByMsId(prefix + "insert") instanceof InsertTemplate);
        Assert.assertNull(helper.getMapperTemplateByMsId(prefix + "customMethod"));
    }
}
