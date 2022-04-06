# MyBatis 通用 Mapper4

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper)

通用Mapper都可以极大的方便开发人员。可以随意的按照自己的需要选择通用方法，还可以很方便的开发自己的通用方法。

极其方便的使用MyBatis单表的增删改查。

支持单表操作，不支持通用的多表联合查询。

**通用 Mapper 支持 Mybatis-3.2.4 及以上版本。**

## 4.2.2-SNAPSHOT - 2022-04-06

- 更新 parent 依赖版本
- 升级 mybatis 为 3.5.9
- 升级 hsqldb 为 2.5.2
- 升级 jps 依赖，使用 2.2 （GAV全变了）
- 修改 test 中的 log4j 为 logback
- 格式化代码

## 4.2.1 - 2022-2-27

4.2.0 版本中缺少了 gitee 仓库中的两个合并请求，针对这部分代码，再次发布 4.2.1 版本。

- `orderByDesc`只有最后一个字段是倒序，改为所有参与`orderby`的字段都倒序 Mr 轩少/V1.1.5-orderByDesc-fix
- 增加Lombok 的`@SuperBuilder、@NoArgsConstructor、@AllArgsConstructor` 注解 tingwen 2020/12/26 18:29 8c816794

## 4.2.0 - 2022-2-26

本次更新最大改动就是统一了所有模块的版本，所有版本都升级为 4.2.0，在之前本项目一共有3个版本号，本次升级前后的版本如下：

- mapper, mapper-all, mapper-all-dependencies 从 4.1.5 升级为 4.2.0
- mapper-core, mapper-base, mapper-extra, mapper-generator, mapper-spring, mapper-weekend 从 1.1.5 升级为 4.2.0
- mapper-spring-boot-starter 相关模块从 2.1.5 升级为 4.2.0

本次更新是 2019年1月28日发布 4.1.5 之后的首次发布，此次更新的内容基本上都来自所有热心开发人员的PR，大部分PR都是功能增强或新功能。

- `WeekendSqls` or部分方法参数 String->Object taiyi* 2021/11/29 19:39 1aa5eff6
- 改进对null的查询 改进对空集合的查询 改进对like的查询 Cheng.Wei* 2020/3/19 0:24 1523d57f
- 改进查询对null值的处理策略 Cheng.Wei* 2020/3/19 0:07 afb6ffc8
- Update FieldHelper.java kong-ly* 2020/3/17 16:06 4a5675d6
- 修复一个错误，该错误可能导致 `EntityHelper.entityTableMap` 被错误清空 glacier* 2020/4/1 18:29 8c57af04
- 扩展一些根据属性及条件值删除的通用方法 jingkaihui* 2020/7/20 17:25 3bf2e1a0
- 修复 `Example.Criteria` 未设置 `where` 条件查询时，带有逻辑删除注解的表报错问题，fixed #722 jingkaihui* 2020/7/20 15:31 570ef154
- 修改生成的getter方法注释中@return 列名为@return 属性名 wanglei* 2018/1/24 11:04 b08258bc
- 更新地址 https://mybatis.io abel533 2020/7/27 21:52 ba417dc3
- 扩展一些根据属性及条件值查询的通用方法 jingkaihui* 2019/10/19 22:14 bd101038
- 添加日志输出异常和警告信息，fixed #IXNLU isea533 2019/6/18 22:03 1764748e
- 合并 pr #17 isea533 2019/5/30 21:53 8d7819e3
- [新增] `Weekend`对象增加`excludeProperties、selectProperties、orderBy、withCountProperty` 支持lambda表达式写属性名
  使用withXXXXX的命名方式链式设置属性 wugh 2019/5/30 14:41 3e25bb9b
- [bug修复] 修复`generateDefaultInstanceMethod`参数,生成的实体类的`defaultInstance`静态方式. 1.存在默认值为''::character varying问题 2.支持基本类型
  wugh 2019/5/30 13:58 cf3e40aa
- [新增] 生成实体类的时候,使用表注释创建类的注释 wugh 2019/5/30 11:28 b4acbf48
- 增加`lombokEqualsAndHashCodeCallSuper`配置，当使用lombok扩展的`EqualsAndHashCode`注解时，可通过此配置（true）为此注解添加`“callSuper = true”`
  ，这对于有继承父类的实体类，如增加支持动态表名时，有用。 calvinit 2019/2/14 13:52 ae901608

## 还会有 MyBatis 通用 Mapper5 吗？

通用 Mapper 每次大的版本，基本上都是底层上的大变化，在使用通用 Mapper 的过程中，有很多人遇到过配置的问题，因为底层实现的方式，所以无法避免配置，而且随着功能的增加，配置也增加了不少。

为了从根本上简化通用方法的实现，从2018年就开始思考如何让实现和MyBatis的兼容性更好，让实现变的更简单，为了从 MyBatis 根本解决问题，给官方提过好几个 PR，在 2019年3月份给 MyBatis 提交的
[pr#1391](https://github.com/mybatis/mybatis-3/pull/1391) 合并后（对应 3.5.1 版本，最低要求版本），终于能以更简单的方式来实现通用 Mapper 了。

由于此次变动太大，因此不打算对 **通用Mapper4** 进行任何改动，从头实现了一个新的项目，名字仍然没有新意的使用了 `mybatis-mapper`，这个项目也发布很久了，由于工作太忙，没精力像以前那样频繁更新， 所以一直没推广新版
mybatis-mapper，如果你动手能力强，喜欢看源码，你也可以试试这个项目：

- [GitHub](https://github.com/mybatis-mapper/mapper)
- [Gitee](https://gitee.com/mybatis-mapper/mapper)
- [文档: https://mapper.mybatis.io](https://mapper.mybatis.io/)
- [开发过程](https://mapper.mybatis.io/releases/1.0.0.html)
- [快速上手](https://mapper.mybatis.io/docs/1.getting-started.html#%E4%BB%8B%E7%BB%8D)

另外，通用 Mapper 中的大量 PR 都是增加的新方法和代码生成器相关的注解，这些和核心无关，因此 mybaits-mapper 会提供独立的项目接收所有新增的通用方法， 和代码生成器相关的 lombok
注解完全不需要了，使用新版本中提供的代码生成器可以更方便的进行定制。

## [**快速入门 - MyBatis 为什么需要通用 Mapper ?**](https://blog.csdn.net/isea533/article/details/83045335)

简介: 在早期项目文档中有过类似主题的内容，但是最近我自己看文档的时候发现一个问题，文档虽然很详细，但是并不适合初次接触的人。为了方便第一次听说，第一次尝试的开发人员了解通用 Mapper，补充此文档。

强烈建议初学者阅读本文，先从整体上了解 通用 Mapper，然后再通过下面的文档更深入的了解。

## [**通用 Mapper 进阶实例：为什么好久都没更新了？**](https://blog.csdn.net/isea533/article/details/104776347)

通过本文，希望读者能有收获，能根据自己的需要设计通用方法，不要只是为了偷懒将自己局限在已有的通用方法中。大而全的通用方法不一定适合自己，根据自己需要选择和设计的通用方法才更满足自己的需要。

## 项目文档

- [文档 - Gitee](https://gitee.com/free/Mapper/wikis/Home)

- [文档 - GitHub](https://github.com/abel533/Mapper/wiki)

- [JavaDoc](https://apidoc.gitee.com/free/Mapper/)

- [更新日志 - Gitee](https://gitee.com/free/Mapper/wikis/changelog)

- [更新日志 - GitHub](https://github.com/abel533/Mapper/wiki/changelog)

## 作者信息

MyBatis 工具网站:[https://mybatis.io](https://mybatis.io)

作者博客：http://blog.csdn.net/isea533

作者邮箱： abel533@gmail.com

如需加群，请通过 https://mybatis.io 首页按钮加群。

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

## 作者新书：《MyBatis 从入门到精通》

![MyBatis 从入门到精通](https://github.com/mybatis-book/book/raw/master/book.png)

### 简介

本书中从一个简单的 MyBatis 查询入手，搭建起学习 MyBatis 的基础开发环境。 通过全面的示例代码和测试讲解了在 MyBatis XML 方式和注解方式中进行增、删、改、查操作的基本用法，介绍了动态 SQL
在不同方面的应用以及在使用过程中的最佳实践方案。 针对 MyBatis 高级映射、存储过程和类型处理器提供了丰富的示例，通过自下而上的方法使读者更好地理解和掌握MyBatis 的高级用法，同时针对 MyBatis
的代码生成器提供了详细的配置介绍。 此外，本书还提供了缓存配置、插件开发、Spring、Spring Boot 集成的详细内容。 最后通过介绍 Git 和 GitHub 让读者了解MyBatis 开源项目，通过对 MyBatis
源码和测试用例的讲解让读者更好掌握 MyBatis。

### 购买地址：

- [京东](https://item.jd.com/12103309.html)

- [当当](http://product.dangdang.com/25098208.html)

- [亚马逊](https://www.amazon.cn/MyBatis从入门到精通-刘增辉/dp/B072RC11DM/ref=sr_1_18?ie=UTF8&qid=1498007125&sr=8-18&keywords=mybatis)

### 相关介绍

- CSDN博客：http://blog.csdn.net/isea533/article/details/73555400

- GitHub项目：https://github.com/mybatis-book/book
