# MyBatis通用Mapper3

通用Mapper都可以极大的方便开发人员。可以随意的按照自己的需要选择通用方法，还可以很方便的开发自己的通用方法。

极其方便的使用MyBatis单表的增删改查。

支持单表操作，不支持通用的多表联合查询。

## 通用 Mapper 支持 Mybatis-3.2.4 及以上版本
## <span style="color:red">特别强调</span>
- **不是表中字段的属性必须加 `@Transient` 注解**
- **通用 Mapper 不支持 devtools 热加载**，devtools 排除实体类包即可，配置方式参考：[using-boot-devtools-customizing-classload](http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html#using-boot-devtools-customizing-classload)

## 项目文档

在你打算使用通用 Mapper 前，一定要看看下面的文档，许多人在初次使用时遇到的问题，99% 都在文档中有说明！！

1. [如何集成通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/2.Integration.md)
2. [如何使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/3.Use.md)
2. [3.3.0版本新增功能用法文档](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/3.2.Use330.md)
3. [根据需要自定义接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/4.Professional.md)
4. [Mapper3通用接口大全](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/5.Mappers.md)
5. [扩展通用接口](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/6.MyMapper.md)
6. [使用Mapper专用的MyBatis生成器插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/7.UseMBG.md)
7. [在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper2/blob/master/wiki/mapper/4.Spring4.md)
8. [Mapper3常见问题和用法](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/9.QA.md)

### 如何让作者为你开发通用方法？

实际上，只要你看看上面的第 6 个文档，你完全可以自己开发出来。

或者你可以通过赞助作者 10~50 元来让作者根据你的需求开发**一个**通用方法。

赞助后保留截图，将截图和需求内容发邮件到 abel533@gmail.com 和作者联系。

你还可以通过开源中国众包购买服务[开发 MyBatis 通用 Mapper 通用方法](https://zb.oschina.net/market/opus/92cda9e3bc85365f)

## 通用 Mapper - 简单用法示例

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

## 实体类注解

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
    
[使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/wiki/mapper3/7.UseMBG.md) 可以方便的生成这些（带注解的）实体类。

## 使用 Maven
```xml
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper</artifactId>
    <version>3.4.0</version>
</dependency>
```
如果你使用 Spring Boot 可以直接引入：
```xml
<!--mapper-->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```
具体用法可以参考：[MyBatis-Spring-Boot](https://github.com/abel533/MyBatis-Spring-Boot) 

## 引入 Jar 包，下载地址：

https://oss.sonatype.org/content/repositories/releases/tk/mybatis/mapper

http://repo1.maven.org/maven2/tk/mybatis/mapper

由于通用Mapper依赖JPA，所以还需要下载persistence-api-1.0.jar：

http://repo1.maven.org/maven2/javax/persistence/persistence-api/1.0/

## [更新日志](http://git.oschina.net/free/Mapper/blob/master/wiki/Changelog.md)

##作者信息

MyBatis 工具网站:[http://mybatis.tk](http://www.mybatis.tk)

作者博客：http://blog.csdn.net/isea533

作者邮箱： abel533@gmail.com

Mybatis工具群（推荐）： <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=7c2f018e4cddc7d4aad04fc312b2d69361a0a896a4f59219a7914953a57bffc2"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Mybatis工具群(2)" title="Mybatis工具群(2)"></a>

Mybatis工具群（2000 人已满）： <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=29e4cce8ac3c65d14a1dc40c9ba5c8e71304f143f3ad759ac0b05146e0952044"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Mybatis工具" title="Mybatis工具"></a>

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)