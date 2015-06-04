#Mybatis通用EntityMapper

##极其方便的使用Mybatis单表的增删改查

##支持单表操作，不支持通用的多表联合查询

##重要提示

EntityMapper是通用Mapper2.x版本中的一部分，通用Mapper3.x以后将EntityMapper移出了通用Mapper，所以EntityMapper独立出来。

建议使用通用Mapper，通用Mapper3更强大，通用方法更多，更方便扩展。

通用Mapper地址：http://git.oschina.net/free/Mapper

##EntityMapper

EntityMapper可以像Hibernate的session一样操纵全部的实体类，由于可以操纵全部实体，因此不能使用二级缓存。EntityMapper支持通用的Example查询和MGB生成的Example查询。

示例代码：

    //获取CommonMapper，继而包装成EntityMapper
    CommonMapper commonMapper = sqlSession.getMapper(CommonMapper.class);
    EntityMapper entityMapper = new EntityMapper(commonMapper);
    //通用Example查询
    Example example = new Example(Country.class);
    //id > 100 && id <= 150
    example.createCriteria().andGreaterThan("id", 100).andLessThanOrEqualTo("id", 150);
    //查询总数
    int count = entityMapper.countByExample(example);
    Assert.assertEquals(50, count);
    
    example = new Example(Country.class);
    //countryname like 'A%'
    example.createCriteria().andLike("countryname", "A%");
    //查询总数
    count = entityMapper.countByExample(example);
    Assert.assertEquals(12, count);

##实体类注解

从上面效果来看也能感觉出这是一种类似hibernate的用法，因此也需要实体和表对应起来，因此使用了JPA注解。更详细的内容可以看下面的<b>项目文档</b>。

Country代码：

    public class Country {
        @Id
        private Integer id;
        @Column
        private String countryname;
        private String countrycode;
        //省略setter和getter方法
    }
    
[使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/5.UseMBG.md) 可以方便的生成这些（带注解的）实体类。

##通用EntityMapper支持Mybatis-3.2.4及以上版本

##[更新日志](http://git.oschina.net/free/EntityMapper/blob/master/wiki/Changelog.md)

##Maven坐标以及下载地址

###最新版本1.0.0 - 2015-06-xx

* xx

如果你使用Maven，只需要添加如下依赖：

```xml
<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>entitymapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

如果你想引入Jar包，你可以从下面的地址下载：

https://oss.sonatype.org/content/repositories/releases/com/github/abel533/mapper/

http://repo1.maven.org/maven2/com/github/abel533/mapper/

EntityMapper依赖JPA，所以还需要下载persistence-api-1.0.jar：

http://repo1.maven.org/maven2/javax/persistence/persistence-api/1.0/

EntityMapper还依赖通用Mapper-3.0.0.jar:

https://oss.sonatype.org/content/repositories/releases/com/github/abel533/mapper/

http://repo1.maven.org/maven2/com/github/abel533/mapper/

##项目文档

###EntityMapper(单一Mapper操作全部实体)

1. [如何集成EntityMapper](http://git.oschina.net/free/Mapper/blob/master/wiki/entity/1.Integration.md)

2. [如何使用EntityMapper](http://git.oschina.net/free/Mapper/blob/master/wiki/entity/2.Use.md)

3. [如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/5.UseMBG.md)

###如何使用SqlMapper

1. [如何使用SqlMapper](http://git.oschina.net/free/Mapper/blob/master/wiki/UseSqlMapper.md)

##作者信息

作者博客：http://blog.csdn.net/isea533

作者邮箱： abel533@gmail.com

Mybatis工具群： 211286137 (Mybatis相关工具插件等等)

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)