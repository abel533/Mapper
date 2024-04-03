package tk.mybatis.solon;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.solon.integration.MybatisAdapterDefault;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;
import org.noear.solon.core.PropsConverter;
import org.noear.solon.core.VarHolder;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * @title: TkMybatis Adapter
 * @author: trifolium.wang
 * @date: 2024/4/1
 * @since 2.7.3
 */
public class TkMapperMybatisAdapter extends MybatisAdapterDefault {

    protected Config tkConfig;

    protected MapperHelper mapperHelper;

    protected TkMapperMybatisAdapter(BeanWrap dsWrap) {
        super(dsWrap);

        dsWrap.context().getBeanAsync(Config.class, bean -> {
            tkConfig = bean;
        });

        dsWrap.context().getBeanAsync(MapperHelper.class, bean -> {
            mapperHelper = bean;
        });
    }

    protected TkMapperMybatisAdapter(BeanWrap dsWrap, Props dsProps) {
        super(dsWrap, dsProps);

        dsWrap.context().getBeanAsync(Config.class, bean -> {
            tkConfig = bean;
        });

        dsWrap.context().getBeanAsync(MapperHelper.class, bean -> {
            mapperHelper = bean;
        });
    }

    @Override
    protected void initConfiguration(Environment environment) {
        config = new tk.mybatis.mapper.session.Configuration();
        config.setEnvironment(environment);

        Props mybatisProps = dsProps.getProp("configuration");
        if (!mybatisProps.isEmpty()) {
            PropsConverter.global().convert(mybatisProps, config, Configuration.class, null);
        }
    }

    @Override
    public SqlSessionFactory getFactory() {
        if (factory == null) {
            builderMapperHelper();
            factory = factoryBuilder.build(config);
        }
        return factory;
    }

    @Override
    public void injectTo(VarHolder varH) {
        super.injectTo(varH);

        //@Db("db1") Config tkConfig;
        if (Config.class.isAssignableFrom(varH.getType())) {
            varH.setValue(this.tkConfig);
        }

        //@Db("db1") tk.mybatis.mapper.session.Configuration configuration;
        if (tk.mybatis.mapper.session.Configuration.class.isAssignableFrom(varH.getType())) {
            varH.setValue(getConfiguration());
        }

        //@Db("db1") MapperHelper mapperHelper;
        if (MapperHelper.class.isAssignableFrom(varH.getType())) {
            varH.setValue(this.mapperHelper);
        }
    }

    /**
     * 通过使用 tk.mybatis.mapper.session.Configuration
     * 替换 MyBatis 中的 org.apache.ibatis.session.Configuration.
     * 重写原 Configuration 中的 addMappedStatement实现
     */
    private void builderMapperHelper() {
        Props cfgProps = dsProps.getProp("tk.mapper");

        if (tkConfig == null) {
            tkConfig = new Config();
        }

        if (!cfgProps.isEmpty()) {
            PropsConverter.global().convert(cfgProps, tkConfig, Config.class, null);
        }
        if (mapperHelper == null) {
            mapperHelper = new MapperHelper();
        }

        mapperHelper.setConfig(tkConfig);
        ((tk.mybatis.mapper.session.Configuration) config).setMapperHelper(mapperHelper);
    }
}
