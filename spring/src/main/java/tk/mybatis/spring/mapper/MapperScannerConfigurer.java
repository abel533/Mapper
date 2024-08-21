/*
 * Copyright 2010-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tk.mybatis.spring.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.*;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.util.Assert.notNull;

/**
 * BeanDefinitionRegistryPostProcessor that searches recursively starting from a base package for interfaces and
 * registers them as {@code MapperFactoryBean}. Note that only interfaces with at least one method will be registered;
 * concrete classes will be ignored.
 * <p>
 * This class was a {code BeanFactoryPostProcessor} until 1.0.1 version. It changed to
 * {@code BeanDefinitionRegistryPostProcessor} in 1.0.2. See https://jira.springsource.org/browse/SPR-8269 for the
 * details.
 * <p>
 * The {@code basePackage} property can contain more than one package name, separated by either commas or semicolons.
 * <p>
 * This class supports filtering the mappers created by either specifying a marker interface or an annotation. The
 * {@code annotationClass} property specifies an annotation to search for. The {@code markerInterface} property
 * specifies a parent interface to search for. If both properties are specified, mappers are added for interfaces that
 * match <em>either</em> criteria. By default, these two properties are null, so all interfaces in the given
 * {@code basePackage} are added as mappers.
 * <p>
 * This configurer enables autowire for all the beans that it creates so that they are automatically autowired with the
 * proper {@code SqlSessionFactory} or {@code SqlSessionTemplate}. If there is more than one {@code SqlSessionFactory}
 * in the application, however, autowiring cannot be used. In this case you must explicitly specify either an
 * {@code SqlSessionFactory} or an {@code SqlSessionTemplate} to use via the <em>bean name</em> properties. Bean names
 * are used rather than actual objects because Spring does not initialize property placeholders until after this class
 * is processed.
 * <p>
 * Passing in an actual object which may require placeholders (i.e. DB user password) will fail. Using bean names defers
 * actual object creation until later in the startup process, after all placeholder substitution is completed. However,
 * note that this configurer does support property placeholders of its <em>own</em> properties. The
 * <code>basePackage</code> and bean name properties all support <code>${property}</code> style substitution.
 * <p>
 * Configuration sample:
 *
 * <pre class="code">
 * {@code
 *   <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
 *       <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
 *       <!-- optional unless there are multiple session factories defined -->
 *       <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
 *   </bean>
 * }
 * </pre>
 *
 * @author Hunter Presnall
 * @author Eduardo Macarron
 *
 * @see MapperFactoryBean
 * @see ClassPathMapperScanner
 */
public class MapperScannerConfigurer
        implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

    private String basePackage;

    private boolean addToConfig = true;

    private String lazyInitialization;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionTemplate sqlSessionTemplate;

    private String sqlSessionFactoryBeanName;

    private String sqlSessionTemplateBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private List<TypeFilter> excludeFilters;

    private List<Map<String, String>> rawExcludeFilters;

    private Class<? extends MapperFactoryBean> mapperFactoryBeanClass;

    private ApplicationContext applicationContext;

    private String beanName;

    private boolean processPropertyPlaceHolders;

    private BeanNameGenerator nameGenerator;

    private String defaultScope;

    private MapperHelper mapperHelper = new MapperHelper();

    private String mapperHelperBeanName;

    public MapperHelper getMapperHelper() {
        return mapperHelper;
    }

    public void setMapperHelper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }


    /**
     * This property lets you set the base package for your mapper interface files.
     * <p>
     * You can set more than one package by using a semicolon or comma as a separator.
     * <p>
     * Mappers will be searched for recursively starting in the specified package(s).
     *
     * @param basePackage
     *          base package name
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Same as {@code MapperFactoryBean#setAddToConfig(boolean)}.
     *
     * @param addToConfig
     *          a flag that whether add mapper to MyBatis or not
     *
     * @see MapperFactoryBean#setAddToConfig(boolean)
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * Set whether enable lazy initialization for mapper bean.
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @param lazyInitialization
     *          Set the @{code true} to enable
     *
     * @since 2.0.2
     */
    public void setLazyInitialization(String lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
    }

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @param annotationClass
     *          annotation class
     */
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified interface class as a
     * parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @param superClass
     *          parent class
     */
    public void setMarkerInterface(Class<?> superClass) {
        this.markerInterface = superClass;
        if (Marker.class.isAssignableFrom(superClass)) {
            mapperHelper.registerMapper(superClass);
        }
    }

    /**
     * Specifies which types are not eligible for the mapper scanner.
     * <p>
     * The scanner will exclude types that define with excludeFilters.
     *
     * @since 3.0.3
     *
     * @param excludeFilters
     *          list of TypeFilter
     */
    public void setExcludeFilters(List<TypeFilter> excludeFilters) {
        this.excludeFilters = excludeFilters;
    }

    /**
     * In order to support process PropertyPlaceHolders.
     * <p>
     * After parsed, it will be added to excludeFilters.
     *
     * @since 3.0.3
     *
     * @param rawExcludeFilters
     *          list of rawExcludeFilter
     */
    public void setRawExcludeFilters(List<Map<String, String>> rawExcludeFilters) {
        this.rawExcludeFilters = rawExcludeFilters;
    }

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     *
     * @deprecated Use {@link #setSqlSessionTemplateBeanName(String)} instead
     *
     * @param sqlSessionTemplate
     *          a template of SqlSession
     */
    @Deprecated
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     * Note bean names are used, not bean references. This is because the scanner loads early during the start process and
     * it is too early to build mybatis object instances.
     *
     * @since 1.1.0
     *
     * @param sqlSessionTemplateName
     *          Bean name of the {@code SqlSessionTemplate}
     */
    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
    }

    /**
     * 属性注入
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        mapperHelper.setProperties(properties);
    }

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     *
     * @deprecated Use {@link #setSqlSessionFactoryBeanName(String)} instead.
     *
     * @param sqlSessionFactory
     *          a factory of SqlSession
     */
    @Deprecated
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     * Note bean names are used, not bean references. This is because the scanner loads early during the start process and
     * it is too early to build mybatis object instances.
     *
     * @since 1.1.0
     *
     * @param sqlSessionFactoryName
     *          Bean name of the {@code SqlSessionFactory}
     */
    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
    }

    /**
     * Specifies a flag that whether execute a property placeholder processing or not.
     * <p>
     * The default is {@literal false}. This means that a property placeholder processing does not execute.
     *
     * @since 1.1.1
     *
     * @param processPropertyPlaceHolders
     *          a flag that whether execute a property placeholder processing or not
     */
    public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
        this.processPropertyPlaceHolders = processPropertyPlaceHolders;
    }

    /**
     * The class of the {@link MapperFactoryBean} to return a mybatis proxy as spring bean.
     *
     * @param mapperFactoryBeanClass
     *          The class of the MapperFactoryBean
     *
     * @since 2.0.1
     */
    public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * Gets beanNameGenerator to be used while running the scanner.
     *
     * @return the beanNameGenerator BeanNameGenerator that has been configured
     *
     * @since 1.2.0
     */
    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    /**
     * Sets beanNameGenerator to be used while running the scanner.
     *
     * @param nameGenerator
     *          the beanNameGenerator to set
     *
     * @since 1.2.0
     */
    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    /**
     * Sets the default scope of scanned mappers.
     * <p>
     * Default is {@code null} (equiv to singleton).
     * </p>
     *
     * @param defaultScope
     *          the default scope
     *
     * @since 2.0.6
     */
    public void setDefaultScope(String defaultScope) {
        this.defaultScope = defaultScope;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        if (this.processPropertyPlaceHolders) {
            processPropertyPlaceHolders();
        }

        var scanner = new ClassPathMapperScanner(registry, getEnvironment());
        scanner.setAddToConfig(this.addToConfig);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setMarkerInterface(this.markerInterface);
        scanner.setExcludeFilters(this.excludeFilters = mergeExcludeFilters());
        scanner.setSqlSessionFactory(this.sqlSessionFactory);
        scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
        scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
        scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
        if (StringUtils.hasText(lazyInitialization)) {
            scanner.setLazyInitialization(Boolean.parseBoolean(lazyInitialization));
        }
        if (StringUtils.hasText(defaultScope)) {
            scanner.setDefaultScope(defaultScope);
        }
        if (StringUtils.hasText(mapperHelperBeanName)) {
            scanner.setMapperHelperBeanName(mapperHelperBeanName);
        }
        scanner.registerFilters();
        //设置通用 Mapper
        scanner.setMapperHelper(this.mapperHelper);
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    /*
     * BeanDefinitionRegistries are called early in application startup, before BeanFactoryPostProcessors. This means that
     * PropertyResourceConfigurers will not have been loaded and any property substitution of this class' properties will
     * fail. To avoid this, find any PropertyResourceConfigurers defined in the context and run them on this class' bean
     * definition. Then update the values.
     */
    private void processPropertyPlaceHolders() {
        Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class,
                false, false);

        if (!prcs.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
            var mapperScannerBean = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
                    .getBeanDefinition(beanName);

            // PropertyResourceConfigurer does not expose any methods to explicitly perform
            // property placeholder substitution. Instead, create a BeanFactory that just
            // contains this mapper scanner and post process the factory.
            var factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = getPropertyValue("basePackage", values);
            this.sqlSessionFactoryBeanName = getPropertyValue("sqlSessionFactoryBeanName", values);
            this.sqlSessionTemplateBeanName = getPropertyValue("sqlSessionTemplateBeanName", values);
            this.lazyInitialization = getPropertyValue("lazyInitialization", values);
            this.defaultScope = getPropertyValue("defaultScope", values);
            this.rawExcludeFilters = getPropertyValueForTypeFilter("rawExcludeFilters", values);
        }
        this.basePackage = Optional.ofNullable(this.basePackage).map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionFactoryBeanName = Optional.ofNullable(this.sqlSessionFactoryBeanName)
                .map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionTemplateBeanName = Optional.ofNullable(this.sqlSessionTemplateBeanName)
                .map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.lazyInitialization = Optional.ofNullable(this.lazyInitialization).map(getEnvironment()::resolvePlaceholders)
                .orElse(null);
        this.defaultScope = Optional.ofNullable(this.defaultScope).map(getEnvironment()::resolvePlaceholders).orElse(null);
    }

    private Environment getEnvironment() {
        return this.applicationContext.getEnvironment();
    }

    private String getPropertyValue(String propertyName, PropertyValues values) {
        var property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        var value = property.getValue();

        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return value.toString();
        }
        if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> getPropertyValueForTypeFilter(String propertyName, PropertyValues values) {
        var property = values.getPropertyValue(propertyName);
        Object value;
        if (property == null || (value = property.getValue()) == null || !(value instanceof List<?>)) {
            return null;
        }
        return (List<Map<String, String>>) value;
    }

    private List<TypeFilter> mergeExcludeFilters() {
        List<TypeFilter> typeFilters = new ArrayList<>();
        if (this.rawExcludeFilters == null || this.rawExcludeFilters.isEmpty()) {
            return this.excludeFilters;
        }
        if (this.excludeFilters != null && !this.excludeFilters.isEmpty()) {
            typeFilters.addAll(this.excludeFilters);
        }
        try {
            for (Map<String, String> typeFilter : this.rawExcludeFilters) {
                typeFilters.add(
                        createTypeFilter(typeFilter.get("type"), typeFilter.get("expression"), this.getClass().getClassLoader()));
            }
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("ClassNotFoundException occur when to load the Specified excludeFilter classes.",
                    exception);
        }
        return typeFilters;
    }

    @SuppressWarnings("unchecked")
    private TypeFilter createTypeFilter(String filterType, String expression, @Nullable ClassLoader classLoader)
            throws ClassNotFoundException {

        if (this.processPropertyPlaceHolders) {
            expression = this.getEnvironment().resolvePlaceholders(expression);
        }

        switch (filterType) {
            case "annotation":
                Class<?> filterAnno = ClassUtils.forName(expression, classLoader);
                if (!Annotation.class.isAssignableFrom(filterAnno)) {
                    throw new IllegalArgumentException(
                            "Class is not assignable to [" + Annotation.class.getName() + "]: " + expression);
                }
                return new AnnotationTypeFilter((Class<Annotation>) filterAnno);
            case "custom":
                Class<?> filterClass = ClassUtils.forName(expression, classLoader);
                if (!TypeFilter.class.isAssignableFrom(filterClass)) {
                    throw new IllegalArgumentException(
                            "Class is not assignable to [" + TypeFilter.class.getName() + "]: " + expression);
                }
                return (TypeFilter) BeanUtils.instantiateClass(filterClass);
            case "assignable":
                return new AssignableTypeFilter(ClassUtils.forName(expression, classLoader));
            case "regex":
                return new RegexPatternTypeFilter(Pattern.compile(expression));
            case "aspectj":
                return new AspectJTypeFilter(expression, classLoader);
            default:
                throw new IllegalArgumentException("Unsupported filter type: " + filterType);
        }
    }

    public void setMapperHelperBeanName(String mapperHelperBeanName) {
        this.mapperHelperBeanName = mapperHelperBeanName;
    }

    /**
     * 从环境变量中获取 mapper 配置信息
     *
     * @param environment
     */
    public void setMapperProperties(Environment environment) {
        Config config = SpringBootBindUtil.bind(environment, Config.class, Config.PREFIX);
        if (mapperHelper == null) {
            mapperHelper = new MapperHelper();
        }
        if (config != null) {
            mapperHelper.setConfig(config);
        }
    }

    /**
     * 从 properties 数组获取 mapper 配置信息
     *
     * @param properties
     */
    public void setMapperProperties(String[] properties) {
        if (mapperHelper == null) {
            mapperHelper = new MapperHelper();
        }
        Properties props = new Properties();
        for (String property : properties) {
            property = property.trim();
            int index = property.indexOf("=");
            if (index < 0) {
                throw new MapperException("通过 @MapperScan 注解的 properties 参数配置出错:" + property + " !\n"
                        + "请保证配置项按 properties 文件格式要求进行配置，例如：\n"
                        + "properties = {\n"
                        + "\t\"mappers=tk.mybatis.mapper.common.Mapper\",\n"
                        + "\t\"notEmpty=true\"\n"
                        + "}"
                );
            }
            props.put(property.substring(0, index).trim(), property.substring(index + 1).trim());
        }
        mapperHelper.setProperties(props);
    }

}
