# Mybatis 通用 Mapper Jar 集成

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-all-dependencies/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-all-dependencies)

本项目默认集成了 mapper-core, mapper-extra, mapper-generator, mapper-spring, mapper-weekend 项目。

使用时，只需要添加 mapper-all 的依赖即可。

```xml
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-all-dependencies</artifactId>
    <version>版本号</version>
</dependency>
```

引入后会自动引入其他依赖。

可以通过在 pom 中增加下面对应的属性来修改依赖的版本号： 

```xml
<properties>
    <mapper-core.version>4.0.0-SNAPSHOT</mapper-core.version>
    <mapper-extra.version>1.0.0-SNAPSHOT</mapper-extra.version>
    <mapper-spring.version>1.0.0-SNAPSHOT</mapper-spring.version>
    <mapper-weekend.version>1.1.3-SNAPSHOT</mapper-weekend.version>
    <mapper-generator.version>1.0.0-SNAPSHOT</mapper-generator.version>
</properties>
```

>上面具体版本号只是示例，默认不需要自己设置。