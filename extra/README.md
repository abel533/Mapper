# Mybatis 通用 Mapper 扩展方法

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-extra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-extra)

## 扩展方法介绍

目前只有从 mapper 3.5.0 中挪出来的 `insertList` 方法。

### InsertListMapper

批量插入，支持批量插入的数据库都可以使用，例如 mysql,h2 等

SQL 形如 `insert table(xxx) values (xxx), (xxx) ...`