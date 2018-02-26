# Mybatis 通用 Mapper 代码生成器

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper-generator)

整个插件只有很少代码是和通用 Mapper 相关的，并且也没有直接的依赖关系。

这个代码生成器实际上是对 MyBatis Generator 的一个扩展，使用这个扩展可以很方便的使用 Freemarker 模板语言编写代码。

## 测试

在 src/test/java 下面，`tk.mybatis.mapper.generator` 包下面有一个测试类 `Generator`。

可以直接运行这个测试类查看生成代码的效果。所有生成的代码在 `src/test/java/test` 目录下，方便删除。

测试使用的 hsqldb 内存数据库，数据库建表 SQL 在 src/test/resources 下面的 `CreateDB.sql` 中。

代码生成器的配置在 `generatorConfig.xml` 中。


# 代码生成器文档

代码生成器是基于 MBG 插件的，所以需要配合 MBG 使用。

一个简单的 MBG 配置如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <property name="javaFileEncoding" value="UTF-8"/>
        <!--配置是否使用通用 Mapper 自带的注释扩展，默认 true-->
        <!--<property name="useMapperCommentGenerator" value="false"/>-->

        <!--通用 Mapper 插件，可以生成带注解的实体类-->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.hsqldb.HsqldbMapper"/>
            <property name="caseSensitive" value="true"/>
            <property name="forceAnnotation" value="true"/>
            <property name="beginningDelimiter" value="`"/>
            <property name="endingDelimiter" value="`"/>
        </plugin>

        <!--通用代码生成器插件-->
        <plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
            <property name="targetProject" value="src/test/java"/>
            <property name="targetPackage" value="test.mapper"/>
            <property name="templatePath" value="generator/mapper.ftl"/>
            <property name="mapperSuffix" value="Dao"/>
            <property name="fileName" value="${tableClass.shortClassName}${mapperSuffix}.java"/>
        </plugin>

        <jdbcConnection driverClass="org.hsqldb.jdbcDriver"
                                connectionURL="jdbc:hsqldb:mem:generator"
                                userId="sa"
                                password="">
        </jdbcConnection>

        <!--MyBatis 生成器只需要生成 Model-->
        <javaModelGenerator targetPackage="test.model" targetProject="./src/test/java"/>

        <table tableName="user%">
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>
    </context>
</generatorConfiguration>
```
在这个配置中，我们只关注 `tk.mybatis.mapper.generator.TemplateFilePlugin`。

## 基于模板的插件 `TemplateFilePlugin`

这个插件中除了几个必备的属性外，还可以增加任意的属性，属性完全是为了给模板提供数据。

先看一个基本完整的配置：

```xml
<!--测试输出单个文件，每个表都会生成一个对应的文件-->
<plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
    <property name="singleMode" value="false"/>
    <property name="targetProject" value="src/test/resources"/>
    <property name="targetPackage" value=""/>
    <property name="templatePath" value="generator/test-one.ftl"/>
    <property name="fileName" value="${tableClass.shortClassName}Test.txt"/>
    <!--默认值是下面这个，可以不配置-->
    <property name="templateFormatter" value="tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter"/>
</plugin>
```

下面介绍必备的属性。

### 1. `targetProject`

用于指定目标项目，一般是 `src/main/java` 或者 `src/main/resource` 这样的目录。
还可以是 `src/test/java` 或者 `src/test/resource` 这样的目录。

在多模块项目中，还能通过相对路径指定为其他的目录，例如：

```xml
<property name="targetProject" value="../myproject-api/src/main/java"/>
```

**这个属性值有一个要求，就是目录必须存在，否则不会生成代码!**

### 2. `targetPackage`

用于指定包的部分，虽然是这个名字，实际上就是路径。

**这个属性指定的路径如果不存在，就会自动创建。**

这个属性的值可以为空。

例如 `mapper/admin` 用于生成 `mapper/admin/` 目录，或者 `tk.mybatis.mapper` 生成包（本质上还是目录）。

这个属性还有一个特殊的地方，它还支持使用模板，就和下面的 `fileName` 一样，举个简单的使用场景。

>你可能在生成前端代码的时候，希望将表对应的 JSP 生成在自己的一个目录中，此时可以配置为： 
>
>`<property name="targetPackage" value="WEB-INF/jsp/${tableClass.lowerCaseName}/"/>`
>
>模板中可以用到的属性，这里都能用，其他属性后面会介绍。

通过这个路径也能看出来，配置一个插件只能根据模板在一个指定位置(targetProject 和 targetPackage 决定的目录)生成一个文件。

### 3. `templatePath`

指定模板路径，可以是任意能够通过 ClassLoader 能够获取的位置，文件类型没有限制。

例如示例中的 `generator/test-one.ftl`。

**这个属性必须指定，否则不会生成代码!**

### 4. `fileName`

这个属性用于指定生成文件的名字，这个值支持使用模板，例如上面的 `${tableClass.shortClassName}Test.txt`，具体可用的属性会在后面介绍。

**这个属性必须指定，否则不会生成代码!**

### 5. `templateFormatter`

**这个属性可选，默认使用基于 FreeMarker 的实现!**

默认情况下，你需要添加下面的依赖：

```xml
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.23</version>
</dependency>
```

默认的实现类为：`tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter`。

这个类实现了两个接口 `TemplateFormatter, ListTemplateFormatter`。

这俩接口分别对应下面 `singleMode` 参数值的 `true` 和 `false`。

也就是一个表生成一个文件，或者多个表生成一个文件。

对于一般情况下，都是第一种情况。但是在配置文件中，可能会用到多个表的信息。

如果你想使用其他模板引擎，可以自己实现上面的接口。

### 6. `singleMode`

上面已经提过，默认为 `true`。

一个表生成一个文件时，可用属性可以参考 `generator/test-one.ftl`，表的属性在 `tableClass` 中。

多个表生成一个文件时，可用属性可以参考 `generator/test-all.ftl`，所有表的属性在 `tableClassSet` 中，通过遍历可以获取单个的信息。

### 7. 其他你需要的属性

模板中需要的特殊信息都可以通过 `<property>` 方法设置，在模板中直接使用这里定义的属性名来使用，后面例子的中的 `mapperSuffix` 就是这种属性。

## `TemplateFilePlugin` 配置示例

因为模板需要根据业务进行设计，所以这里只提供了两个简单的 mapper 目标和两个完整属性的示例模板。

因为一个模板只能生成一类的文件，所以如果要生成多个不同的文件，就需要配置多个插件。

>这种设计很灵活，因为自由度很高，所以代价就是配置的多。
>
>但是正常情况下，根据业务设计的一套模板基本是固定的，不会有太多变化，所以用起来并不麻烦。

例如下面的示例：

```xml
<!--通用代码生成器插件-->
<!--mapper接口-->
<plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
    <property name="targetProject" value="src/test/java"/>
    <property name="targetPackage" value="test.mapper"/>
    <property name="templatePath" value="generator/mapper.ftl"/>
    <property name="mapperSuffix" value="Dao"/>
    <property name="fileName" value="${tableClass.shortClassName}${mapperSuffix}.java"/>
</plugin>
<!--mapper.xml-->
<plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
    <property name="targetProject" value="src/test/resources"/>
    <property name="targetPackage" value="mappers"/>
    <property name="mapperPackage" value="test.mapper"/>
    <property name="templatePath" value="generator/mapperXml.ftl"/>
    <property name="mapperSuffix" value="Dao"/>
    <property name="fileName" value="${tableClass.shortClassName}${mapperSuffix}.xml"/>
</plugin>
<!--测试输出单个文件，每个表都会生成一个对应的文件-->
<plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
    <property name="targetProject" value="src/test/resources"/>
    <property name="targetPackage" value=""/>
    <property name="templatePath" value="generator/test-one.ftl"/>
    <property name="fileName" value="${tableClass.shortClassName}Test.txt"/>
    <!--默认值是下面这个，可以不配置-->
    <property name="templateFormatter"
              value="tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter"/>
</plugin>
<!--测试输出整个文件，所有表都可用，一次只生成一个文件，用于聚合所有表使用-->
<plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
    <property name="singleMode" value="false"/>
    <property name="targetProject" value="src/test/resources"/>
    <property name="targetPackage" value=""/>
    <property name="templatePath" value="generator/test-all.ftl"/>
    <property name="fileName" value="TestAll.txt"/>
</plugin>
```

前两个会生成 Dao 后缀的 Mapper 接口和 XML，其中有个针对性的参数 `mapperSuffix` 用于配置后缀，
还有个 `mapperPackage` 在生成 XML 时获取接口的包名（因为和这里的 `targetPackage` 可以不同）。

后两个插件用于演示所有可用的属性，而且是两种不同的模式。

在表和实体上可用的所有属性如下：

```
特殊：targetPackage值在 ${package} 中。

<!-- 详细日期用法参考：http://freemarker.apache.org/docs/ref_builtins_date.html -->
当前时间：
<#assign dateTime = .now>
日期：${dateTime?date}
时间：${dateTime?time}
格式化：${dateTime?string["yyyy-MM-dd HH:mm:ss"]}


所有配置的属性信息:
<#list props?keys as key>
${key} - ${props[key]}
</#list>

实体和表的信息：
表名：${tableClass.tableName}
变量名：${tableClass.variableName}
小写名：${tableClass.lowerCaseName}
类名：${tableClass.shortClassName}
全名：${tableClass.fullClassName}
包名：${tableClass.packageName}

列的信息：
=====================================
<#if tableClass.pkFields??>
主键：
    <#list tableClass.pkFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

<#if tableClass.baseFields??>
基础列：
    <#list tableClass.baseFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

<#if tableClass.blobFields??>
Blob列：
    <#list tableClass.blobFields as field>
    -------------------------------------
    列名：${field.columnName}
    列类型：${field.jdbcType}
    字段名：${field.fieldName}
    注释：${field.remarks}
    类型包名：${field.typePackage}
    类型短名：${field.shortTypeName}
    类型全名：${field.fullTypeName}
    是否主键：${field.identity?c}
    是否可空：${field.nullable?c}
    是否为BLOB列：${field.blobColumn?c}
    是否为String列：${field.stringColumn?c}
    是否为字符串列：${field.jdbcCharacterColumn?c}
    是否为日期列：${field.jdbcDateColumn?c}
    是否为时间列：${field.jdbcTimeColumn?c}
    是否为序列列：${field.sequenceColumn?c}
    列长度：${field.length?c}
    列精度：${field.scale}
    </#list>
</#if>

=====================================
全部列（包含了pk,base,blob 字段，可用的属性和上面的一样）：
<#if tableClass.allFields??>
列名 - 字段名
    <#list tableClass.allFields as field>
    ${field.columnName} - ${field.fieldName}
    </#list>
</#if>
```

## 测试执行

上面示例就是本项目的测试代码，在 `src/test/resources/generator/generatorConfig.xml` 中。

还提供了一种 Java 编码方式运行的类，`src/test/java/` 中的 `tk.mybatis.mapper.generator.Generator`，配置上面 xml 中的数据库信息就可以生成。

测试生成的**部分**结果如下。

实体：
```java
@Table(name = "`user_info`")
public class UserInfo {
    @Id
    @Column(name = "`Id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;
```

Dao：
```java
package test.mapper;

import test.model.UserInfo;

/**
* 通用 Mapper 代码生成器
*
* @author mapper-generator
*/
public interface UserInfoDao extends tk.mybatis.mapper.common.Mapper<UserInfo> {

}
```

XML：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="test.mapper.UserInfoDao">

</mapper>
```

test-one.ftl 生成的信息如下：
```java
目标package: 

当前时间：
2017-11-6
22:00:45
2017-11-06 22:00:45

所有配置的属性信息:
targetPackage - 
templateFormatter - tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter
templatePath - generator/test-one.ftl
targetProject - src/test/resources
fileName - ${tableClass.shortClassName}Test.txt

实体和表的信息：
表名：user_info
变量名：userInfo
小写名：userinfo
类名：UserInfo
全名：test.model.UserInfo
包名：test.model

列的信息：
=====================================
主键：
    -------------------------------------
    列名：Id
    列类型：INTEGER
    字段名：id
    注释：
    类型包名：java.lang
    类型短名：Integer
    类型全名：java.lang.Integer
    是否主键：true
    是否可空：false
    是否为BLOB列：false
    是否为String列：false
    是否为字符串列：false
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：10
    列精度：0

基础列：
    -------------------------------------
    列名：username
    列类型：VARCHAR
    字段名：username
    注释：用户名
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：false
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：32
    列精度：0
    -------------------------------------
    列名：password
    列类型：VARCHAR
    字段名：password
    注释：密码
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：32
    列精度：0
    -------------------------------------
    列名：usertype
    列类型：VARCHAR
    字段名：usertype
    注释：用户类型
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：2
    列精度：0
    -------------------------------------
    列名：enabled
    列类型：INTEGER
    字段名：enabled
    注释：是否可用
    类型包名：java.lang
    类型短名：Integer
    类型全名：java.lang.Integer
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：false
    是否为字符串列：false
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：10
    列精度：0
    -------------------------------------
    列名：realname
    列类型：VARCHAR
    字段名：realname
    注释：真实姓名
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：32
    列精度：0
    -------------------------------------
    列名：qq
    列类型：VARCHAR
    字段名：qq
    注释：QQ
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：14
    列精度：0
    -------------------------------------
    列名：email
    列类型：VARCHAR
    字段名：email
    注释：
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：100
    列精度：0
    -------------------------------------
    列名：tel
    列类型：VARCHAR
    字段名：tel
    注释：联系电话
    类型包名：java.lang
    类型短名：String
    类型全名：java.lang.String
    是否主键：false
    是否可空：true
    是否为BLOB列：false
    是否为String列：true
    是否为字符串列：true
    是否为日期列：false
    是否为时间列：false
    是否为序列列：false
    列长度：255
    列精度：0

Blob列：

=====================================
全部列：
列名 - 字段名
    Id - id
    username - username
    password - password
    usertype - usertype
    enabled - enabled
    realname - realname
    qq - qq
    email - email
    tel - tel
```

## 最后

基础的代码生成器是很简单的，和 Java 拼字符串输出很像，这里只是使用了模板。

几乎所有人都在 JSP 中用过的 EL 就是一种模板，可能你会 `<c:forEach ` 这种，但是联想不到这里的代码生成器而已。
 
后续会在 https://github.com/abel533/Mybatis-Spring 项目中提供一套模板做为示例。

>自从 http://mybatis.tk 改版后，捐赠列表好久都没更新过了，如果你觉得这个插件和本文有用，可以小小的捐赠一笔。
>
>支付宝：
>
>![alipay](http://mybatis.tk/images/alipay.png)
>
>微信：
>
>![weixinpay](http://mybatis.tk/images/weixinpay.png)
