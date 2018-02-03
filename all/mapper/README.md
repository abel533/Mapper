# Mybatis 通用 Mapper Jar 集成

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper)

本项目默认集成了 mapper-core, mapper-extra, mapper-generator, mapper-spring, mapper-weekend 项目。

使用时，只需要添加 mapper 的依赖即可。

```xml
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper</artifactId>
    <version>版本号</version>
</dependency>
```

引入该依赖时不会引入传递依赖。