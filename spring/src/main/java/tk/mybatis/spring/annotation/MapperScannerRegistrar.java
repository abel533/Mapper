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
package tk.mybatis.spring.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.ClassPathMapperScanner;
import tk.mybatis.spring.mapper.MapperFactoryBean;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link ImportBeanDefinitionRegistrar} to allow annotation configuration of MyBatis mapper scanning. Using
 * an @Enable annotation allows beans to be registered via @Component configuration, whereas implementing
 * {@code BeanDefinitionRegistryPostProcessor} will work for XML configuration.
 *
 * @author Michael Lanyon
 * @author Eduardo Macarron
 * @author Putthiphong Boonphong
 *
 * @see MapperFactoryBean
 * @see ClassPathMapperScanner
 *
 * @since 1.2.0
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    public static final Logger         LOGGER = LoggerFactory.getLogger(MapperScannerRegistrar.class);

    // Note: Do not move resourceLoader via cleanup
    private             ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        var mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        if (mapperScanAttrs != null) {
            registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry,
                    generateBaseBeanName(importingClassMetadata, 0));
        }
    }

    void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs,
                                 BeanDefinitionRegistry registry, String beanName) {

        var builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", annoAttrs.getBoolean("processPropertyPlaceHolders"));

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            builder.addPropertyValue("annotationClass", annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            builder.addPropertyValue("markerInterface", markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
        }

        var sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
        if (StringUtils.hasText(sqlSessionTemplateRef)) {
            builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
        }

        var sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
        if (StringUtils.hasText(sqlSessionFactoryRef)) {
            builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
        }

        List<String> basePackages = new ArrayList<>(Arrays.stream(annoAttrs.getStringArray("basePackages"))
                .filter(StringUtils::hasText).collect(Collectors.toList()));

        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                .collect(Collectors.toList()));

        if (basePackages.isEmpty()) {
            basePackages.add(getDefaultBasePackage(annoMeta));
        }

        var excludeFilterArray = annoAttrs.getAnnotationArray("excludeFilters");
        if (excludeFilterArray.length > 0) {
            List<TypeFilter> typeFilters = new ArrayList<>();
            List<Map<String, String>> rawTypeFilters = new ArrayList<>();
            for (AnnotationAttributes excludeFilters : excludeFilterArray) {
                if (excludeFilters.getStringArray("pattern").length > 0) {
                    // in oder to apply placeholder resolver
                    rawTypeFilters.addAll(parseFiltersHasPatterns(excludeFilters));
                } else {
                    typeFilters.addAll(typeFiltersFor(excludeFilters));
                }
            }
            builder.addPropertyValue("excludeFilters", typeFilters);
            builder.addPropertyValue("rawExcludeFilters", rawTypeFilters);
        }

        //优先级 mapperHelperRef > properties > springboot
        String mapperHelperRef = annoAttrs.getString("mapperHelperRef");
        String[] properties = annoAttrs.getStringArray("properties");
        if (StringUtils.hasText(mapperHelperRef)) {
            builder.addPropertyValue("mapperHelperBeanName", mapperHelperRef);
        } else if (properties != null && properties.length > 0) {
            builder.addPropertyValue("mapperProperties", properties);
        } else {
            try {
                builder.addPropertyValue("mapperProperties", this.environment);
            } catch (Exception e) {
                LOGGER.warn("只有 Spring Boot 环境中可以通过 Environment(配置文件,环境变量,运行参数等方式) 配置通用 Mapper，" +
                        "其他环境请通过 @MapperScan 注解中的 mapperHelperRef 或 properties 参数进行配置!" +
                        "如果你使用 tk.mybatis.mapper.session.Configuration 配置的通用 Mapper，你可以忽略该错误!", e);
            }
        }

        var lazyInitialization = annoAttrs.getString("lazyInitialization");
        if (StringUtils.hasText(lazyInitialization)) {
            builder.addPropertyValue("lazyInitialization", lazyInitialization);
        }

        var defaultScope = annoAttrs.getString("defaultScope");
        if (!AbstractBeanDefinition.SCOPE_DEFAULT.equals(defaultScope)) {
            builder.addPropertyValue("defaultScope", defaultScope);
        }

        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

        // for spring-native
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

    }

    /**
     * Parse excludeFilters which FilterType is REGEX or ASPECTJ
     *
     * @param filterAttributes
     *          AnnotationAttributes of excludeFilters
     *
     * @since 3.0.3
     */
    private List<Map<String, String>> parseFiltersHasPatterns(AnnotationAttributes filterAttributes) {

        List<Map<String, String>> rawTypeFilters = new ArrayList<>();
        FilterType filterType = filterAttributes.getEnum("type");
        var expressionArray = filterAttributes.getStringArray("pattern");
        for (String expression : expressionArray) {
            switch (filterType) {
                case REGEX:
                case ASPECTJ:
                    Map<String, String> typeFilter = new HashMap<>(16);
                    typeFilter.put("type", filterType.name().toLowerCase());
                    typeFilter.put("expression", expression);
                    rawTypeFilters.add(typeFilter);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot specify the 'pattern' attribute if use the " + filterType
                            + " FilterType in exclude filter of @MapperScan");
            }
        }
        return rawTypeFilters;
    }

    /**
     * Parse excludeFilters which FilterType is ANNOTATION ASSIGNABLE or CUSTOM
     *
     * @param filterAttributes
     *          AnnotationAttributes of excludeFilters
     *
     * @since 3.0.3
     */
    private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {

        List<TypeFilter> typeFilters = new ArrayList<>();
        FilterType filterType = filterAttributes.getEnum("type");

        for (Class<?> filterClass : filterAttributes.getClassArray("value")) {
            switch (filterType) {
                case ANNOTATION:
                    Assert.isAssignable(Annotation.class, filterClass,
                            "Specified an unsupported type in 'ANNOTATION' exclude filter of @MapperScan");
                    @SuppressWarnings("unchecked")
                    var annoClass = (Class<Annotation>) filterClass;
                    typeFilters.add(new AnnotationTypeFilter(annoClass));
                    break;
                case ASSIGNABLE_TYPE:
                    typeFilters.add(new AssignableTypeFilter(filterClass));
                    break;
                case CUSTOM:
                    Assert.isAssignable(TypeFilter.class, filterClass,
                            "An error occured when processing a @ComponentScan " + "CUSTOM type filter: ");
                    typeFilters.add(BeanUtils.instantiateClass(filterClass, TypeFilter.class));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot specify the 'value' or 'classes' attribute if use the "
                            + filterType + " FilterType in exclude filter of @MapperScan");
            }
        }
        return typeFilters;
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
    }

    private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }

    /**
     * A {@link MapperScannerRegistrar} for {@link MapperScans}.
     *
     * @since 2.0.0
     */
    static class RepeatingRegistrar extends MapperScannerRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            var mapperScansAttrs = AnnotationAttributes
                    .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScans.class.getName()));
            if (mapperScansAttrs != null) {
                var annotations = mapperScansAttrs.getAnnotationArray("value");
                for (var i = 0; i < annotations.length; i++) {
                    registerBeanDefinitions(importingClassMetadata, annotations[i], registry,
                            generateBaseBeanName(importingClassMetadata, i));
                }
            }
        }
    }

}
