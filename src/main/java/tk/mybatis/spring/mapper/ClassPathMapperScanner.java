package tk.mybatis.spring.mapper;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.util.Set;

/**
 * 重写ClassPathMapperScanner
 *
 * 主要在doScan方法中注入mapperHelper和tk.xxx.MapperFactoryBean
 *
 * @author liuzh
 */
public class ClassPathMapperScanner extends org.mybatis.spring.mapper.ClassPathMapperScanner {

    private MapperHelper mapperHelper;

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        //注入mapperHelper
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("mapperHelper", this.mapperHelper);
            definition.setBeanClass(MapperFactoryBean.class);
        }
        return beanDefinitions;
    }
}
