package tk.mybatis.mapper.autoconfigure;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * Callback interface that can be customized a {@link SqlSessionFactoryBean} object generated on auto-configuration.
 *
 * @author Kazuki Shimizu
 * @since 2.2.2
 */
@FunctionalInterface
public interface SqlSessionFactoryBeanCustomizer {

    /**
     * Customize the given a {@link SqlSessionFactoryBean} object.
     *
     * @param factoryBean the factory bean object to customize
     */
    void customize(SqlSessionFactoryBean factoryBean);

}
