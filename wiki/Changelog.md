#更新日志

###3.1.2 - 2015-07-14

* 解决别名时的一种特殊情况，例如`@Column(name="`desc`")`的时候，就不需要自动添加别名
* 反射获取所有列名的时候，不在自动转换为大写形式，对数据库区分大小写的情况有用

##3.1.1 - 2015-07-01

* 解决`ConditionMapper`中`selectByCondition`和`updateByCondition`方法错误

##3.1.0 - 2015-06-10

* 基础包名从`com.github.abel533`改为`tk.mybatis.mapper`
* Maven的groupId改为`tk.mybatis`,artifactId为`mapper`
* 增加和Example功能类似的Condition查询，仅仅名字不同
* 更多详细变化请看[Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)
* 关于3.0.x版本请看[Mapper3.0.x](http://git.oschina.net/free/Mapper/tree/Mapper3.0.x/)

##3.0.0 - 2015-06-04

* 将`EntityMapper`和`SqlMapper`移出，现在是独立项目[EntityMapper](http://git.oschina.net/free/EntityMapper)
* 将`Mapper<T>`全部接口方法拆分为独立接口，方便选择集成
* 增加`MySqlMapper<T>`包含批量插入和单个插入，批量插入可以回写全部id
* 增加`RowBoundsMapper<T>`包含两个分页查询，可以配合[PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)实现物理分页
* 详细变化请看[Mapper3变化](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/1.Changes.md)
* Mapper2资深用户请看[Mapper3高级应用](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/4.Professional.md)
* [Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)
* [快速开发自己的通用接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/6.MyMapper.md)


##2.3.4 - 2015-06-01

* 高并发时selectKey会产生异常，解决[#32](http://git.oschina.net/free/Mapper/issues/32)

* 兼容MyBatis3.3.0版本

* <b>提前预告：下个版本3.0.0会将通用Mapper项目拆分为两个项目，会有一些大的改动</b>

##2.3.3 - 2015-05-14

* 解决Example查询中的`and`缺少空格的问题

* 去掉UUID和JDBC两种主键策略类型中对字段类型的限制
  不再限制为`String`，可以是任意简单类型，需要自己保证类型匹配。例如UUID配置的策略可以返回`Integer`,那么字段类型必须是`Integer`。

* JDBC类型的主键策略可以配置多个，就相当于`keyProperties="id1,id2..."`

* `EntityHelper`的`getOrderByClause`方法返回值从`StringBuilder`改为`String`，解决`@OrderBy`注解时的异常

* <b>提前预告：下个版本3.0.0会将通用Mapper项目拆分为两个项目，会有一些大的改动</b>

##2.3.2 - 2015-04-21

* 解决Example查询中in,notin无效的bug[#24](http://git.oschina.net/free/Mapper/issues/24)

##2.3.1 - 2015-04-13

* 完善所有和PrimaryKey有关的通用查询

* 修复Mapper<T>接口中update操作会更新主键的bug

* 修复Mapper<T>接口中使用Example查询的时候，条件and前面缺少空格，影响美观

* MBG插件增加caseSensitive默认false，当数据库表名区分大小写时，可以将该属性设置为true

##2.3.0 - 2015-04-05

* Mapper接口和EntityMapper都增加了`selectOne`方法，该查询返回值最多只能有一个，存在多个时抛出异常

* Mapper接口和EntityMapper中，返回List的查询方法都支持JPA的`@Orderby`注解。其中`Example`查询中的`orderby`会覆盖注解的`@Orderby`设置。

* 通过实体类获取表名的时候，不对表名进行强制的大小写转换。如果数据库大小写敏感，请通过`@Table`注解和数据库保持一致。

##2.2.0 - 2015-03-11

* 新增`SqlMapper`，可以使用MyBatis直接执行sql，[详细文档](http://git.oschina.net/free/Mapper/blob/master/wiki/UseSqlMapper.md)

##v2.1.0 - 2015-03-07

* 通用Mapper接口增加Example查询方法，包括以下方法：

    int selectCountByExample(Object example);

    int deleteByExample(Object example);

    List<T> selectByExample(Object example);

    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);

    int updateByExample(@Param("record") T record, @Param("example") Object example);

* 通用`Example`增加了一个`exists`的参数，当`true`的时候如果使用的字段不存在会抛出异常，`false`时不抛出异常，但是不使用该字段的条件。

##V2.0.1 - 2015-02-28

* 增加拦截器，完善相应的文档

##V2.0.0 - 2015-02-04

* 增加一个`CommonMapper`和包装类`EntityMapper`，建议使用`EntityMapper`
* 有关`EntityMapper`的内容请看独立文档，这个类足以独立成一个开源项目
* 增加对JPA注解`OrderBy`的支持，仅对`select`一个方法有效

简单说明，为什么版本这么快就到了2.0?，因为`EntityMapper`，这是另一种形式的通用Mapper。

这里说说`EntityMapper`和通用Mapper的区别。

通用Mapper需要有继承的接口，需要指定泛型类型，可以缓存，和手写的效果一样。

`EntityMapper`无需继承，可以直接使用，而且这一个对象就可以操作全部的实体对象（和通用Mapper注解要求一样，不支持主键策略）和表，是一个更接近Hibernate用法的类，这个类非常强大，支持Mybatis生成的Example查询，还支持一个通用Example查询。

`EntityMapper`功能更全面，但是不支持主键策略，由于该类足以独立成一个开源项目，简单几句不能说明用法，因此详细内容请看独立的文档。

##V1.1.0

* 完善文档
* 解决主键selectKey的一个bug
* 解决@Column注解为空时的bug
* 完善自动增长的配置，增加对JDBC的支持`@GeneratedValue(generator = "JDBC")`,详细请看下面关于主键策略的详细内容
* 增加了一个`notEmpty`参数，该参数会影响所有使用`getAllIfColumnNode`方法的地方,具体到`Mapper<T>`,影响3个方法：select,selectCount,delete。如果设置为`true`，那么`<if ...`的条件中会包含`String`类型`property!=''`的条件。

##v1.0.0正式发布版

* 增加通用Mapper专用的MyBatis Generator插件，可以自动生成实体类注解以及Mapper接口和一个空的xml文件

* 插件后续可能会增加更多的自动生成代码。

* 有关插件的使用，后续完善文档

##v0.3.2版本说明

移除了`MapperInterceptor`类，不在提供拦截器方式的使用。如果有需要可以自己从0.3.1版本获取。

##v0.3.1版本说明

支持Spring4泛型注入，详细请看文档[在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md) 

##v0.3.0版本说明

这个版本的主要目的是消除拦截器，因此针对常用的情况增加了两种更方便的使用方式。


##v0.2.0版本说明

该版本做了大量的重构，在原有基础上增加了两个类，分别为`MapperTemplate`和`MapperProvider`，其他几个类都有相当大的改动。  

**但是**，这次重构并不影响原有的业务代码。  

这次重构的目的是为了方便开发者自行扩展，增加自己需要的通用Mapper。这次重构后，扩展变的更容易。稍后会写一篇**如何进行扩展**的文档。  

这次更新还修复Oracle序列的BUG。