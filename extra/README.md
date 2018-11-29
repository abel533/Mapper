# Mybatis 通用 Mapper 扩展方法

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-extra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-extra)

## 扩展方法介绍

### InsertListMapper

批量插入

- 支持批量插入的数据库都可以使用，例如 mysql,h2 等

    `tk.mybatis.mapper.additional.insert.InsertListMapper`

    SQL 形如 `insert table(xxx) values (xxx), (xxx) ...`

- Oracle特殊批量插入
    `tk.mybatis.mapper.additional.dialect.oracle.InsertListMapper`

    SQL 形如 
    ```sql
     INSERT ALL
     INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     INTO demo_country ( country_id,country_name,country_code ) VALUES ( ?,?,? )
     SELECT 1 FROM DUAL
    ```

    **由于语法限制，暂不支持序列.**

### UpdateByPrimaryKeySelectiveForceMapper

空字段强制更新

针对`UpdateByPrimaryKeySelectiveMapper`中， 空值也需要设置的场景提供的解决方案。

参见: [https://github.com/abel533/Mapper/issues/133](https://github.com/abel533/Mapper/issues/133)