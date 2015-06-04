#MyBatis通用Mapper3

##极其方便的使用MyBatis单表的增删改查

##支持单表操作，不支持通用的多表联合查询

##优点?

通用Mapper都可以极大的方便开发人员。可以随意的按照自己的需要选择通用方法，还可以很方便的开发自己的通用方法。

为了让您更方便的了解这通用Mapper，这里贴一段代码来看实际效果。

##通用Mapper2.x

[Mapper2.x版本](http://git.oschina.net/free/Mapper/tree/Mapper2.x) 是2.x版本，目前最新的3.x版本改动很大，如果正在使用2.x版本，可以去看2.x版本的文档。

##通用Mapper3

全部针对单表操作，每个实体类都需要继承通用Mapper接口来获得通用方法。

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
    countryList = mapper.selectByExample(example2);
    Assert.assertEquals(83, countryList.size());

CountryMapper代码如下：

    public interface CountryMapper extends Mapper<Country> {
    }

这里不说更具体的内容，如果您有兴趣，可以查看下面的<b>项目文档</b>

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

###最新版本3.0.0 - 2015-06-xx

* 大量变化

如果你使用Maven，只需要添加如下依赖：

```xml
<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>mapper</artifactId>
    <version>3.0.0</version>
</dependency>
```

如果你想引入Jar包，你可以从下面的地址下载：

https://oss.sonatype.org/content/repositories/releases/com/github/abel533/mapper/

http://repo1.maven.org/maven2/com/github/abel533/mapper/

由于通用Mapper依赖JPA，所以还需要下载persistence-api-1.0.jar：

http://repo1.maven.org/maven2/javax/persistence/persistence-api/1.0/

##项目文档

###通用Mapper 3.x.x

1. [Mapper3变化](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/1.Changes.md)

2. [如何集成通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/1.Integration.md)

3. [如何使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper/2.Use.md)

4. [高级应用](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/4.Professional.md)

5. [Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)

6. [快速开发自己的通用接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/6.MyMapper.md)

###通用Mapper 2.x.x

[http://git.oschina.net/free/Mapper/tree/Mapper2.x](http://git.oschina.net/free/Mapper/tree/Mapper2.x)

##作者信息

作者博客：http://blog.csdn.net/isea533

作者邮箱： abel533@gmail.com

Mybatis工具群： 211286137 (Mybatis相关工具插件等等)

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)