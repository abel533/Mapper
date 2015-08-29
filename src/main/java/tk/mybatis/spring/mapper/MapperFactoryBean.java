package tk.mybatis.spring.mapper;

import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * 增加mapperHelper
 *
 * @param <T>
 * @author liuzh
 */
public class MapperFactoryBean<T> extends org.mybatis.spring.mapper.MapperFactoryBean<T> {

    private MapperHelper mapperHelper;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        //通用Mapper
        if (mapperHelper.isExtendCommonMapper(getObjectType())) {
            mapperHelper.processConfiguration(getSqlSession().getConfiguration(), getObjectType());
        }
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }
}
