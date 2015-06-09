package tk.mybatis.mapper.mapperhelper;

/**
 * 空方法Mapper接口默认MapperTemplate
 *
 * 如BaseSelectMapper，接口纯继承，不包含任何方法
 */
public class EmptyMapperProvider extends MapperTemplate {

    public EmptyMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public boolean supportMethod(String msId) {
        return false;
    }
}
