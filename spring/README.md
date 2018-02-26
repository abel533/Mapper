# Mybatis 通用 Mapper 和 Spring 集成

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-spring/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-spring)

## 依赖

项目自身依赖不传递(`<scope>provided</scope>`)，因此需要自己提供其他依赖：

> 正常情况下，也是在这些依赖基础上增加的 mapper-spring

```xml
<!-- 需要添加的依赖 -->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring</artifactId>
    <version>版本号</version>
</dependency>
```

其他依赖

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>版本号</version>
</dependency>
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper</artifactId>
    <version>版本号</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>版本号</version>
</dependency>

<!-- Spring -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>版本号</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>版本号</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>版本号</version>
</dependency>
```

## 配置

本项目主要提供了两种大的配置方式。

- `MapperScannerConfigurer` xml bean 配置
- `@MapperScan` 注解

除此之外，高版本的 MyBatis (3.4.0+) 和 mybatis-spring (1.3.0+) 中还有一种推荐的 `tk.mybatis.mapper.session.Configuration` 配置。

> 通用 Mapper 3.6.0 之后会自动注册带有 `@RegisterMapper` 注解的**基类**接口，不在强制要求配置 mappers 属性，只需要给你的基类加 `@RegisterMapper` 注解即可。

### 初次使用通用 Mapper 请注意

下面的示例只是演示如何进行配置，具体配置那些参数要自己选择！

所有可配置参数请参考通用 Mapper 文档：

> https://github.com/abel533/Mapper/blob/master/wiki/mapper3/2.Integration.md

### 一、`MapperScannerConfigurer` xml bean 配置

```xml
<bean class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="tk.mybatis.mapper.mapper"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    <property name="properties">
        <value>
            mappers=tk.mybatis.mapper.common.Mapper
        </value>
    </property>
</bean>
```
注意两点：

 1. 这里使用的 `tk.mybatis.spring.mapper.MapperScannerConfigurer`，不是官方的 `org.xxx`
 2. 所有对通用 Mapper 的配置，参考上面的 mappers=xxx，一行写一个配置即可

### 二、`@MapperScan` 注解

纯注解使用的时候，通用 Mapper 的参数不能像原来那样直接配置，为了适应这种方式，提供了三种可用的方式。

下面按照优先级由高到低的顺序来讲注解配置用法。

#### 1. 使用 `mapperHelperRef` 配置

```java
@Configuration
@MapperScan(value = "tk.mybatis.mapper.mapper", mapperHelperRef = "mapperHelper")
public static class MyBatisConfigRef {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .addScript("CreateDB.sql")
                .build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        return sessionFactory.getObject();
    }

    @Bean
    public MapperHelper mapperHelper() {
        Config config = new Config();
        List<Class> mappers = new ArrayList<Class>();
        mappers.add(Mapper.class);
        config.setMappers(mappers);

        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(config);
        return mapperHelper;
    }
}
```

在这个例子中 `@MapperScan` 唯一特殊的地方在于 `mapperHelperRef` 属性，这个属性用于指定 MapperHelper bean 的 `name`，这里的名字和代码中配置的 `mapperHelper()` 的方法名一致。

>Spring 中默认的 name 就是方法名，还可以通过 `@Bean` 注解指定 `name`。

在这种配置方式中，你可以很方便的控制 `MapperHelper` 中的各项配置。

#### 2. 使用 `properties` 配置

```java
@Configuration
@MapperScan(value = "tk.mybatis.mapper.mapper",
    properties = {
            "mappers=tk.mybatis.mapper.common.Mapper",
            "notEmpty=true"
    }
)
public static class MyBatisConfigProperties {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .addScript("CreateDB.sql")
                .build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        return sessionFactory.getObject();
    }
}
```
如上面代码中所示，这种配置方式和 xml bean 的方式比较接近，就是通过一行一行的 `xx=xxx` 对通用 Mapper 进行配置，配置时参考这里的示例配置即可。

#### 3. Spring Boot 环境中使用 `application.[yml|properties]` 配置文件

在 Spring Boot 中使用 Mapper 时，如果选择使用注解方式（可以不引入 mapper-starter 依赖），就可以选择这第 3 种方式。

>特别提醒：Spring Boot 中常见的是配置文件方式，使用环境变量或者运行时的参数都可以配置，这些配置都可以对通用 Mapper 生效。

例如在 yml 格式中配置：
```yml
mapper:
  mappers:
    - tk.mybatis.mapper.common.Mapper
    - tk.mybatis.mapper.common.Mapper2
  not-empty: true
```

在 propertie 配置中：
```properties
mapper.mappers=tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.common.Mapper2
mapper.not-empty=true
```

>特别提醒：Spring Boot 中支持 relax 方式的参数配置，但是前面两种方式都不支持，前两种配置参数的时候需要保证大小写一致！

### 三、`tk.mybatis.mapper.session.Configuration` 配置

**使用要求：MyBatis (3.4.0+) 和 mybatis-spring (1.3.0+)**

注意该类的包名，这个类继承了 MyBatis 的 `Configuration` 类，并且重写了 `addMappedStatement` 方法，如下：

```java
@Override
public void addMappedStatement(MappedStatement ms) {
    try {
        super.addMappedStatement(ms);
        //在这里处理时，更能保证所有的方法都会被正确处理
        if (this.mapperHelper != null) {
            this.mapperHelper.processMappedStatement(ms);
        }
    } catch (IllegalArgumentException e) {
        //这里的异常是导致 Spring 启动死循环的关键位置，为了避免后续会吞异常，这里直接输出
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}
```

`tk.mybatis.mapper.session.Configuration` 提供了 3 种配置通用 Mapper 的方式，如下所示：
```java
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
```

使用 `tk.mybatis.mapper.session.Configuration` 有两种和 Spring 集成的配置方法

#### 1. Spring XML 配置

配置如下：

```xml
<!--使用 Configuration 方式进行配置-->
<bean id="mybatisConfig" class="tk.mybatis.mapper.session.Configuration">
    <!-- 配置通用 Mapper，有三种属性注入方式 -->
    <property name="mapperProperties">
        <value>
            notEmpty=true
        </value>
    </property>
</bean>

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configuration" ref="mybatisConfig"/>
</bean>

<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="tk.mybatis.mapper.configuration"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
``` 

>特别注意：这种情况下的 MapperScannerConfigurer 是官方 mybatis-spring 中提供的类，不是 tk 开头的！

参考这里的配置即可，注意和其他方式的区别。

这里直接配置一个 tk 中提供的 `Configuration`，然后注入到 `SqlSessionFactoryBean` 中。

#### 2. 注解方式

```java
@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    //创建 Configuration，设置通用 Mapper 配置
    tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
    //有 3 种配置方式
    configuration.setMapperHelper(new MapperHelper());
    sessionFactory.setConfiguration(configuration);
    
    return sessionFactory.getObject();
}
```

看上述代码以及注释。