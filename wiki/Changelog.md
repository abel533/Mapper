# 更新日志

## 3.4.3 - 2017-08-17

* `MapperPlugin` 增加 `forceAnnotation` 参数，默认 `false`，设置为 `true` 后一定会生成`@Table`和`@Column`注解。
* 为实例化 `TypeHandler` 增加 `javaTypeClass` by **junchao**。
* 更新 `Example` 类，在获取 `property` 时，判断有没有该 `property` 并抛出异常 by **chengcheng.feng**。
* 所有类的属性从 `HashMap` 改为 `ConcurrentHashMap`。


## 3.4.2 - 2017-07-19

* 简化Example的xml逻辑，解决由于and,or位置错误导致Example使用空条件时的错误，完善测试

## 3.4.1 - 2017-07-17

* `Example` 增加复杂的 `and` 和 `or` 功能。
* `Example` 增加排除查询字段的方法 `excludeProperties`(`selectProperties`优先级更高) [#261](http://git.oschina.net/free/Mapper/issues/261).
* `SqlHelper` 中复杂的 `if` 改为 `choose` 方式。
* 解决通过`@Column`配置关键字的分隔符时，无法得到该列值的bug。

## 3.4.0 - 2017-02-19

* `Example` 增加 for update 支持，仅能用于 selectByExample 和 selectCountByExample 方法 #210
* `Example.Criteria` 增加 `andAllEqualTo` 方法，将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null #206
* 增加参数 `checkExampleEntityClass`，默认 `false` 用于校验通用 Example 构造参数 entityClass 是否和当前调用的 Mapper<EntityClass> 类型一致 #201
* 增加参数 `useSimpleType`，默认 `false`，启用后判断实体类属性是否为表字段时校验字段是否为简单类型，如果不是就忽略该属性，这个配置优先级高于所有注解
* 增加参数 `simpleTypes`，默认的简单类型在 `SimpleTypeUtil` 中，使用该参数可以增加额外的简单类型，通过逗号隔开的全限定类名添加
* 所有 `RuntimeException` 异常改为 `tk.mybatis.mapper.MapperException` 异常
* 所有 Update 方法添加 `@Options(useCache = false, useGeneratedKeys = false)`，fix #216
* 使用自定义的 `SelectKeyGenerator`，防止有默认值时被替换掉 fix #213
* 将 MapperTemplate 属性改为 protected
* MBG 插件中 generatedKey 元素的 sqlStatement 属性可以配置为形如 select SEQ_{1} from dual 的 SQL，其中 {0} 代表小写的表名，{1} 是大写的表名
   MBG 配置示例如下,类似 Oracle 序列的配置方式：
   ```xml
   <generatedKey column="id" 
        sqlStatement="select SEQ_{1}.nextval from dual" 
        identity="false" 
        type="pre"/>
   ```
   这个配置生成的代码会像下面这样：
   ```java
   public class Author {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY,
               generator = "select SEQ_AUTHOR.nextval from dual")
       private Integer id;
       // 省略其他
   }
   ```

## 3.3.9 - 2016-09-04

* 增加`selectByIds`和`deleteByIds`，用法见通用Mapper接口大全
* 根据**李领北**建议修改`Example`中的`propertyMap`#159
* `Example`中的`andIn`和`andNotIn`中的参数`Collection`改为`Iterable`
* 解决驼峰转下划线的错误，感谢 ptma, piggsoft 和 liufor 的PR
* 增加对MBG1.3.4的支持
* MBG插件支持`beginningDelimiter`和`endingDelimiter`
* MBG插件增加schema配置（catalog也可以用这个），会自动在表的注解名字前面加上`schema.tablename`
* MBG插件支持oracle获取注释，其他数据库可以尝试#114
* MBG扩展，详情看[MyBatis Generator 1.3.4 扩展，可以设置 Mapper（Dao）后缀](http://blog.csdn.net/isea533/article/details/52430691)

## 3.3.8 - 2016-03-23

* `Example`的`andIn`和`andNotIn`方法参数改为`Collection` #109
* 解决ResultMapping.Builder3.2.6版本新增`lazy`方法导致无法兼容3.2.4~3.2.5版本的问题，仍然兼容3.2.4+
* 解决github[#12](https://github.com/abel533/Mapper/issues/12) 问题
* 解决#107
* 解决和分页插件PageHelper中orderBy默认属性名相同导致排序的错误

## 3.3.7 - 2016-03-12

* `Example`增加`orderBy`方法，使用属性进行排序，例如：`example.orderBy("id").desc().orderBy("countryname").orderBy("countrycode").asc();`
* 当实体类包含数组类型的字段时，在`resultMap`中不使用`javaType`，这种情况如果出错，可以通过`@ColumnType`注解设置`jdbcType` #103
* 实体类中忽略`transient`类型的字段#106

## 3.3.6 - 2016-02-20

* 增加对mybatis-spring 1.2.4版本的支持，兼容之前的版本

## 3.3.5 - 2016-02-16

* `Example`增加对动态表名支持，通过`setTableName`设置表名
* 在example相关的两个`update`方法中，参数为实体类和`Example`，这个方法只能通过`Example`来设置动态表名，不支持通过实体设置动态表名
* 优化两个`select count`查询，当表只有一个主键的时候，使用`select count(pk)`，其他时候使用`select count(*)`

## 3.3.4 - 2016-01-05

* 解决insertList的bug#86
* `Example`构造方法增加`notNull`参数，默认`false`，允许值为`null`，值为`null`的时候不加入到条件中。
* `seqFormat`格式化参数增加第四个可配置值`TableName`

## 3.3.3 - 2015-12-30

- 解决OGNL中的and,or大写导致的错误
- 解决SpecialProvider不支持insertable的bug#77
- 解决JDK6,7无法获取字段泛型类型的问题。
- 提供一个Spring Boot集成的示例: https://github.com/abel533/MyBatis-Spring-Boot

## 3.3.2 - 2015-12-12

- 解决数据越界bug#73
- 解决and少空格问题
- 解决order by错误#74
- `tk.mybatis.spring.mapper.MapperScannerConfigurer`中的属性`mapperHelper`增加setter和getter方法，方便通过代码进行配置

## 3.3.1 - 2015-12-09

- 增加`enableMethodAnnotation`参数，可以控制是否支持方法上的JPA注解，默认`false`。
  设置`enableMethodAnnotation = true`的时候注意，如`getRealName`或`setYourName`都会产生`realName`属性或`yourName`属性，如果该方法对应的属性不是表中的字段，就需要给方法增加`@Transient`注解。
  同样如果你的实体是继承`Map`类型的，你不需要在实体中写`private String userName`这样的属性，你只需要写`setUserName`或`getUserName`这样的方法就可以。
- 在处理的注解的时候，优先从`Field`获取，然后是`setter`方法，最后是`getter`方法，注解重复的情况下，只获取按顺序得到的第一个
- 为了支持如`public class Country extends Entity<Integer, String>`这样的泛型类型,在生成`#{propertyName}`的时候都带上了`javaType`属性。
  产生的结果就是`#{propertyName, javaType=java.lang.Integer}`这样子的，这会导致当你调用方法时，必须保证类型一致。
  也就是假设主键是`Integer id`，调用`selectByPrimaryKey(Object id)`的时候，参数`id`必须使用`100`这样的数字，不能使用`"100"`字符串（以前版本可以）。
  如果不带`javaType`，那么如果`id`是个泛型，MyBatis查找的时候就会因为找不到正确的类型而抛出异常。
- 为了让扩展更方便，将`tk.mybatis.mapper.provider`包下所有的通用接口的实现方法改为了`String`形式。
  自己扩展单表操作的方法是非常容易的事情，建议有一定通用Mapper使用基础的自行扩展，扩展可以参考[如何扩展通用接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/6.MyMapper.md)
- 新增`SqlHelper`工具类，其中包含了大量可用的现成的SQL方法
- `@Column`注解增加对`insertable`和`updatable`属性的支持


## 3.3.0 - 2015-11-01

- 增加对动态表名的支持，需要实体类继承`IDynamicTableName`接口，用法见[详细说明](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/3.2.Use330.md)

- `Example`增加自定义查询条件，提供了4个方法，具体方法和用法见[详细说明](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/3.2.Use330.md)

- 新增`@ColumnType`注解，可以单独设置列的`jdbcType`和`typeHandler`

- `Example`的`in`和`not in`中的`List<Object>`参数改为`List<?>`，允许任意类型

- select查询方法返回类型不在使用`resultType`，改为`resultMap`，因此可以支持`typeHandler`的读取

- `Style`自动转方式新增`camelhumpAndUppercase`驼峰转下划线大写形式,`camelhumpAndLowercase`驼峰转下划线小写形式

- MapperTemplate中的`getSelectReturnType`方法改为`getEntityClass`，`getBEFORE`改为`isBEFORE`

- 文档中增加`@GeneratedValue(strategy = GenerationType.IDENTITY)`的一种重要[用法说明](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/3.2.Use330.md)

- 修复selectAll不支持`@OrderBy`注解的bug

- 解决一个驼峰转换bug，例如`helloWorld`会转换为`hello_world`（原先是`hello_World`）

## 3.2.2 - 2015-09-19

* 和Spring集成时，允许通过`markerInterface`属性配置通用接口（注意该属性的原有作用不变），想要让该接口自动注册，该接口就需要继承`tk.mybatis.mapper.common.Marker`接口，`Mapper<T>`默认继承该接口，所以如果自己的接口是继承`Mapper<T>`的，不需要再继承。
* 解决注册默认接口时存在的bug

## 3.2.1 - 2015-09-02

* 解决spring集成中可能出现definition.getBeanClassName()空指针异常bug[#49](http://git.oschina.net/free/Mapper/issues/49)
* 关于3.2.x版本，请仔细看3.2.0的更新日志，最新版本的文档也是针对3.2.x版本的

## 3.2.0 - 2015-09-02

* 移除`MapperInterceptor`拦截器，以后不能在通过拦截器配置
* 增加mybatis-spring特殊支持，主要是根据mybatis-spring项目增加了下面两个类：
   - `tk.mybatis.spring.mapper.MapperScannerConfigurer`
   - `tk.mybatis.spring.mapper.MapperFactoryBean`
* 这两个类和MyBatis提供的区别是增加了MapperHelper属性，通过在`MapperScannerConfigurer`中使用`properties`属性注入配置
* 这两个类，在全名上和MyBatis的区别是`org.mybatis.xxx`改为了`tk.mybatis.xxx`，名字相近，更方便修改配置
* 和Spring集成方法：

```xml
<bean class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.isea533.mybatis.mapper"/>
    <property name="properties">
        <value>
            mappers=tk.mybatis.mapper.common.Mapper
        </value>
    </property>
</bean>
```

* 这种配置方式是不是简单的不能再简单了?
* 增加`style`属性配置，用来配置对象名/字段和表名/字段之间的转换方式，可选值：
   - `normal`:使用实体类名/属性名作为表名/字段名
   - `camelhump`:<b>这是默认值</b>，驼峰转换为下划线形式
   - `uppercase`:转换为大写
   - `lowercase`:转换为小写
* 增加实体注解`@NameStyle`，该注解优先于全局配置`style`
* 解决`example.selectProperties`映射错误的bug[#48](http://git.oschina.net/free/Mapper/issues/48)

## 3.1.3 - 2015-08-25

* 去掉了3.1.3-SNAPSHOT版本中的`MapperOnceInterceptor`拦截器，下个版本会完善`MapperHelper`的配置方式
* `Example`增加了`example.selectProperties("id", "countryname", ...)`方法，可以指定查询列，注意这里参数写的是属性名，`Example`会自动映射到列名
* `Example`增加`andEqualTo(实体对象)`方法，可以将一个实体放进去，会自动根据属性和值拼出column=value的条件 <b>Bob - 0haizhu0@gmail.com 提供</b>
* MyBatis在处理`<cache/>`和`@CacheNamespace`的时候不统一，只有一个能生效，这导致xml中配置二级缓存对通用Mapper注解形式的方法无效，该问题已解决
* 二级缓存配置方法，如果接口有对应的xml，在xml中配置二级缓存。如果只有接口没有xml，用注解配置二级缓存即可
* 需要注意的是，二级缓存在xml配置时，只对通用Mapper方法有效，自己用`@Select`等注解定义的这种仍然无效，这种情况只能在xml中定义

## 3.1.2 - 2015-07-14

* 解决别名时的一种特殊情况，例如`@Column(name="`desc`")`的时候，就不需要自动添加别名
* 反射获取所有列名的时候，不在自动转换为大写形式，对数据库区分大小写的情况有用

## 3.1.1 - 2015-07-01

* 解决`ConditionMapper`中`selectByCondition`和`updateByCondition`方法错误

## 3.1.0 - 2015-06-10

* 基础包名从`com.github.abel533`改为`tk.mybatis.mapper`
* Maven的groupId改为`tk.mybatis`,artifactId为`mapper`
* 增加和Example功能类似的Condition查询，仅仅名字不同
* 更多详细变化请看[Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)
* 关于3.0.x版本请看[Mapper3.0.x](http://git.oschina.net/free/Mapper/tree/Mapper3.0.x/)

## 3.0.0 - 2015-06-04

* 将`EntityMapper`和`SqlMapper`移出，现在是独立项目[EntityMapper](http://git.oschina.net/free/EntityMapper)
* 将`Mapper<T>`全部接口方法拆分为独立接口，方便选择集成
* 增加`MySqlMapper<T>`包含批量插入和单个插入，批量插入可以回写全部id
* 增加`RowBoundsMapper<T>`包含两个分页查询，可以配合[PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)实现物理分页
* 详细变化请看[Mapper3变化](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/1.Changes.md)
* Mapper2资深用户请看[Mapper3高级应用](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/4.Professional.md)
* [Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)
* [快速开发自己的通用接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/6.MyMapper.md)


## 2.3.4 - 2015-06-01

* 高并发时selectKey会产生异常，解决[#32](http://git.oschina.net/free/Mapper/issues/32)

* 兼容MyBatis3.3.0版本

* <b>提前预告：下个版本3.0.0会将通用Mapper项目拆分为两个项目，会有一些大的改动</b>

## 2.3.3 - 2015-05-14

* 解决Example查询中的`and`缺少空格的问题

* 去掉UUID和JDBC两种主键策略类型中对字段类型的限制
  不再限制为`String`，可以是任意简单类型，需要自己保证类型匹配。例如UUID配置的策略可以返回`Integer`,那么字段类型必须是`Integer`。

* JDBC类型的主键策略可以配置多个，就相当于`keyProperties="id1,id2..."`

* `EntityHelper`的`getOrderByClause`方法返回值从`StringBuilder`改为`String`，解决`@OrderBy`注解时的异常

* <b>提前预告：下个版本3.0.0会将通用Mapper项目拆分为两个项目，会有一些大的改动</b>

## 2.3.2 - 2015-04-21

* 解决Example查询中in,notin无效的bug[#24](http://git.oschina.net/free/Mapper/issues/24)

## 2.3.1 - 2015-04-13

* 完善所有和PrimaryKey有关的通用查询

* 修复Mapper<T>接口中update操作会更新主键的bug

* 修复Mapper<T>接口中使用Example查询的时候，条件and前面缺少空格，影响美观

* MBG插件增加caseSensitive默认false，当数据库表名区分大小写时，可以将该属性设置为true

## 2.3.0 - 2015-04-05

* Mapper接口和EntityMapper都增加了`selectOne`方法，该查询返回值最多只能有一个，存在多个时抛出异常

* Mapper接口和EntityMapper中，返回List的查询方法都支持JPA的`@Orderby`注解。其中`Example`查询中的`orderby`会覆盖注解的`@Orderby`设置。

* 通过实体类获取表名的时候，不对表名进行强制的大小写转换。如果数据库大小写敏感，请通过`@Table`注解和数据库保持一致。

## 2.2.0 - 2015-03-11

* 新增`SqlMapper`，可以使用MyBatis直接执行sql，[详细文档](http://git.oschina.net/free/Mapper/blob/master/wiki/UseSqlMapper.md)

## v2.1.0 - 2015-03-07

* 通用Mapper接口增加Example查询方法，包括以下方法：

    int selectCountByExample(Object example);

    int deleteByExample(Object example);

    List<T> selectByExample(Object example);

    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);

    int updateByExample(@Param("record") T record, @Param("example") Object example);

* 通用`Example`增加了一个`exists`的参数，当`true`的时候如果使用的字段不存在会抛出异常，`false`时不抛出异常，但是不使用该字段的条件。

## V2.0.1 - 2015-02-28

* 增加拦截器，完善相应的文档

## V2.0.0 - 2015-02-04

* 增加一个`CommonMapper`和包装类`EntityMapper`，建议使用`EntityMapper`
* 有关`EntityMapper`的内容请看独立文档，这个类足以独立成一个开源项目
* 增加对JPA注解`OrderBy`的支持，仅对`select`一个方法有效

简单说明，为什么版本这么快就到了2.0?，因为`EntityMapper`，这是另一种形式的通用Mapper。

这里说说`EntityMapper`和通用Mapper的区别。

通用Mapper需要有继承的接口，需要指定泛型类型，可以缓存，和手写的效果一样。

`EntityMapper`无需继承，可以直接使用，而且这一个对象就可以操作全部的实体对象（和通用Mapper注解要求一样，不支持主键策略）和表，是一个更接近Hibernate用法的类，这个类非常强大，支持Mybatis生成的Example查询，还支持一个通用Example查询。

`EntityMapper`功能更全面，但是不支持主键策略，由于该类足以独立成一个开源项目，简单几句不能说明用法，因此详细内容请看独立的文档。

## V1.1.0

* 完善文档
* 解决主键selectKey的一个bug
* 解决@Column注解为空时的bug
* 完善自动增长的配置，增加对JDBC的支持`@GeneratedValue(generator = "JDBC")`,详细请看下面关于主键策略的详细内容
* 增加了一个`notEmpty`参数，该参数会影响所有使用`getAllIfColumnNode`方法的地方,具体到`Mapper<T>`,影响3个方法：select,selectCount,delete。如果设置为`true`，那么`<if ...`的条件中会包含`String`类型`property!=''`的条件。

## v1.0.0正式发布版

* 增加通用Mapper专用的MyBatis Generator插件，可以自动生成实体类注解以及Mapper接口和一个空的xml文件

* 插件后续可能会增加更多的自动生成代码。

* 有关插件的使用，后续完善文档

## v0.3.2版本说明

移除了`MapperInterceptor`类，不在提供拦截器方式的使用。如果有需要可以自己从0.3.1版本获取。

## v0.3.1版本说明

支持Spring4泛型注入，详细请看文档[在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md) 

## v0.3.0版本说明

这个版本的主要目的是消除拦截器，因此针对常用的情况增加了两种更方便的使用方式。


## v0.2.0版本说明

该版本做了大量的重构，在原有基础上增加了两个类，分别为`MapperTemplate`和`MapperProvider`，其他几个类都有相当大的改动。  

**但是**，这次重构并不影响原有的业务代码。  

这次重构的目的是为了方便开发者自行扩展，增加自己需要的通用Mapper。这次重构后，扩展变的更容易。稍后会写一篇**如何进行扩展**的文档。  

这次更新还修复Oracle序列的BUG。