package tk.mybatis.mapper.session;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.util.Properties;

/**
 * 使用提供的 Configuration 可以在纯 Java 或者 Spring(mybatis-spring-1.3.0+) 模式中使用
 *
 * @author liuzh
 */
public class Configuration extends org.apache.ibatis.session.Configuration {

    private MapperHelper mapperHelper;

    /**
     * 直接注入 mapperHelper
     *
     * @param mapperHelper
     */
    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    /**
     * 使用属性方式配置
     *
     * @param properties
     */
    public void setMapperProperties(Properties properties) {
        if (this.mapperHelper == null) {
            this.mapperHelper = new MapperHelper();
        }
        this.mapperHelper.setProperties(properties);
    }

    /**
     * 使用 Config 配置
     *
     * @param config
     */
    public void setConfig(Config config) {
        if (mapperHelper == null) {
            mapperHelper = new MapperHelper();
        }
        mapperHelper.setConfig(config);
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        try {
            super.addMappedStatement(ms);
            //没有任何配置时，使用默认配置
            if (this.mapperHelper == null) {
                this.mapperHelper = new MapperHelper();
            }
            this.mapperHelper.processMappedStatement(ms);
        } catch (IllegalArgumentException e) {
            //这里的异常是导致 Spring 启动死循环的关键位置，为了避免后续会吞异常，这里直接输出
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
