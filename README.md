#Mybatis通用Mapper

##极其方便的使用Mybatis单表的增删改查

##优点?

本项目支持两种类型的通用Mapper，这两种Mapper都可以极大的方便开发人员。

为了让您更方便的了解这两种通用Mapper，这里分别贴一段代码来看实际效果。

##通用Mapper

这是本项目提供的第一种通用Mapper，优点是可以缓存，全部针对单表操作，每个实体类都需要继承通用Mapper接口来获得通用方法。

示例代码：

    CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
    //查询全部
    List<Country> countryList = mapper.select(new Country());
    //总数
    Assert.assertEquals(183, countryList.size());

    //通用Example查询
    Example example = new Example(Country.class);
    example.createCriteria().andGreaterThan("id", 100);
    countryList = mapper.selectByExample(example);
    Assert.assertEquals(83, countryList.size());

    //MyBatis-Generator生成的Example查询
    CountryExample example2 = new CountryExample();
    example2.createCriteria().andIdGreaterThan(100);
    countryList = mapper.selectByExample(example);
    Assert.assertEquals(83, countryList.size());

CountryMapper代码如下：

    public interface CountryMapper extends Mapper<Country> {
    }

这里不说更具体的内容，如果您有兴趣，可以查看下面的<b>项目文档</b>

##EntityMapper

这是第二种通用Mapper，EntityMapper可以像Hibernate的session一样操纵全部的实体类，由于可以操纵全部实体，因此不能使用二级缓存。EntityMapper支持通用的Example查询和MGB生成的Example查询。

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

##通用Mapper支持Mybatis-3.2.4及以上版本 

##[更新日志](http://git.oschina.net/free/Mapper/blob/master/wiki/Changelog.md)

##Maven坐标以及下载地址

###当前开发版本2.1.0-SNAPSHOT

通用Mapper接口增加Example查询方法，包括以下方法：

    int selectCountByExample(Object example);

    int deleteByExample(Object example);

    List<T> selectByExample(Object example);

    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);

    int updateByExample(@Param("record") T record, @Param("example") Object example);

###最新版本2.0.1

如果你使用Maven，只需要添加如下依赖：

```xml
<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>mapper</artifactId>
    <version>2.0.1</version>
</dependency>
```

如果你想引入Jar包，你可以从下面的地址下载：

https://oss.sonatype.org/content/repositories/releases/com/github/abel533/mapper/

http://repo1.maven.org/maven2/com/github/abel533/mapper/

由于通用Mapper依赖JPA，所以还需要下载persistence-api-1.0.jar：

http://repo1.maven.org/maven2/javax/persistence/persistence-api/1.0/

##项目文档

###通用Mapper

1. [如何集成通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/1.Integration.md)

2. [如何使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/2.Use.md)

3. [如何开发自己的通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/3.ExtendMapper.md)

4. [在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/4.Spring4.md)

5. [如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/5.UseMBG.md)

###EntityMapper(单一Mapper操作全部实体)

1. [如何集成EntityMapper](http://git.oschina.net/free/Mapper/blob/master/wiki/entity/1.Integration.md)

2. [如何使用EntityMapper](http://git.oschina.net/free/Mapper/blob/master/wiki/entity/2.Use.md)

3. [如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/5.UseMBG.md)

##作者信息

作者博客：http://blog.csdn.net/isea533

作者QQ： 120807756

作者邮箱： abel533@gmail.com

Mybatis工具群： 211286137 (Mybatis相关工具插件等等)

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)