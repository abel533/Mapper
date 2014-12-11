package com.github.abel533.mapper;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Properties;

/**
 * 通用Mapper和Spring集成
 *
 * @author liuzh
 */
public class MapperSpring implements BeanPostProcessor {

    private final MapperHelper mapperHelper = new MapperHelper();

    /**
     * 通过注入配置参数
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        mapperHelper.setProperties(properties);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //对所有的SqlSessionTemplate进行处理，多数据源情况下仍然有效
        if (bean instanceof SqlSessionTemplate) {
            SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) bean;
            mapperHelper.processConfiguration(sqlSessionTemplate.getConfiguration());
        } else if (bean instanceof MapperFactoryBean) {
            MapperFactoryBean mapperFactoryBean = (MapperFactoryBean) bean;
            mapperHelper.processConfiguration(mapperFactoryBean.getSqlSession().getConfiguration());
        }
        return bean;
    }
}