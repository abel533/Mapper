# MyBatis Mapper integration with Spring Boot

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-spring-boot-starter)

Mapper-Spring-Boot-Starter 帮助你集成通用 Mapper 到 Spring Boot。

Mapper-Spring-Boot-Starter will help you use Mapper with Spring Boot.

## How to use
在 pom.xml 中添加如下依赖：

Add the following dependency to your pom.xml: 
```xml
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.2.3</version>
</dependency>
```

## 1.2.3 - 2018-01-24

- 增加 `tk.mybatis.spring.mapper.SpringBootBindUtil`，使用原生方式兼容 Spring Boot 1.x 和 2.x 版本，已经不存在 relax 值问题，以前的配置不需要修改即可使用。
- 特别注意，如果使用了 `@MapperScan` 注解，请使用 `tk.mybatis.spring.annotation.MapperScan` 注解。
- 通用 Mapper 升级到 3.5.2 版本。

## 1.2.2

- 由于 weekend 版本依赖问题，这个版本存在问题，不作为正式发布版本。

## 1.2.1 - 2018-01-10

- 为了增强兼容性，`MapperAutoConfiguration` 增加 `@AutoConfigureBefore(name = "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration")`
即使依赖中存在 MyBatis 的 starter，Mapper 也可以优先使用自动配置，MyBatis后续就不会触发生成 `@Bean`

- 支持简单的 relax 绑定，例如 not-empty 会转换为 notEmpty 使用，兼容 spring boot 配置方式


## 1.2.0 - 2018-01-08

- 通用 Mapper 3.5.0
- 去掉 mybatis-spring-boot-starter 依赖，不在使用 mybatis 官方 starter，使用通用 Mapper 时不要再引入官方 starter
- 参考 mybatis 官方 starter 重新实现，解决 mapper 初始化可能存在的问题 [pr#5 by fengcbo](https://github.com/abel533/mapper-boot-starter/pull/5)
- 如果需要使用 `@MapperScan` 请选择 tk 开头的 `tk.mybatis.spring.annotation.MapperScan`

## 1.1.7 - 2017-12-17

- 通用 Mapper 3.4.6
- spring-boot 1.5.9.RELEASE

## 1.1.6 - 2017-11-11

- 通用 Mapper 3.4.5

## 1.1.5 - 2017-10-21

- 通用 Mapper 3.4.4
- mybatis-starter 1.3.1
- spring-boot 1.5.8.RELEASE

## 1.1.4 - 2017-08-18

- 通用 Mapper 3.4.3

## 1.1.3 - 2017-07-18

- 通用 Mapper 3.4.2

## 1.1.2 - 2017-07-17

- 通用 Mapper 3.4.1
- mybatis 3.4.4
- mybatis-spring-boot 1.3.0
- spring-boot 1.5.4.RELEASE

## 1.1.1 - 2017-03-28

- 解决 1.1.0 版本不一致的问题
- 增加对多数据源的支持，感谢 邱占波的 [PR #2](https://github.com/abel533/mapper-boot-starter/pull/2)

## 1.1.0 - 2017-02-19

- mybatis 升级到 3.4.2
- mapper 升级到 3.4.0
- mybatis-spring 升级到 1.3.1
- mybatis-spring-boot 升级到 1.2.0
- spring-boot 升级到 1.4.4.RELEASE

## Example
>https://github.com/abel533/MyBatis-Spring-Boot

## Special Configurations
一般情况下，你不需要做任何配置。

Normally, you don't need to do any configuration.

如果需要配置，可以使用如下方式进行配置：

You can config PageHelper as the following:

application.properties:
```properties
mapper.propertyName=propertyValue
```

示例:
```properties
mapper.mappers[0]=tk.mybatis.sample.mapper.BaseMapper
mapper.mappers[1]=tk.mybatis.mapper.common.Mapper
```
默认情况下，没有 mappers 配置时，会自动注册 `tk.mybatis.mapper.common.Mapper`

因为通用 Mapper 是固定的属性，所以接收参数使用的对象，按照 Spring Boot 配置规则，大写字母都变了带横线的小写字母。针对如 IDENTITY（对应i-d-e-n-t-i-t-y）提供了全小写的 identity 配置，如果 IDE 能自动提示，看自动提示即可。

IDE 应该可以自动提示：  

![自动提示属性](properties.png)

## MyBatis Mapper
>https://github.com/abel533/Mapper
