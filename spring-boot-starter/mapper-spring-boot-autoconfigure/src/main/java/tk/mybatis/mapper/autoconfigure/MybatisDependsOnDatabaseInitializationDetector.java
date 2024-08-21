package tk.mybatis.mapper.autoconfigure;

import java.util.Collections;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.sql.init.dependency.AbstractBeansOfTypeDependsOnDatabaseInitializationDetector;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitializationDetector;

/**
 * {@link DependsOnDatabaseInitializationDetector} for Mybatis.
 *
 * @author Eddú Meléndez
 * @since 2.3.0
 */
class MybatisDependsOnDatabaseInitializationDetector
        extends AbstractBeansOfTypeDependsOnDatabaseInitializationDetector {

    @Override
    protected Set<Class<?>> getDependsOnDatabaseInitializationBeanTypes() {
        return Collections.singleton(SqlSessionTemplate.class);
    }

}
