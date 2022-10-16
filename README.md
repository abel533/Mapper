# MyBatis 通用 Mapper4

[![Maven central](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/tk.mybatis/mapper)

通用Mapper都可以极大的方便开发人员。可以随意的按照自己的需要选择通用方法，还可以很方便的开发自己的通用方法。

极其方便的使用MyBatis单表的增删改查。

支持单表操作，不支持通用的多表联合查询。

**通用 Mapper 支持 Mybatis-3.2.4 及以上版本。**

## [下一代 通用 Mapper5？](https://mapper.mybatis.io/docs/1.getting-started.html#%E4%BB%8B%E7%BB%8D)

通用 Mapper 每次大的版本，基本上都是底层上的大变化，在使用通用 Mapper 的过程中，有很多人遇到过配置的问题，因为底层实现的方式，所以无法避免配置，而且随着功能的增加，配置也增加了不少。

为了从根本上简化通用方法的实现，从2018年就开始思考如何让实现和MyBatis的兼容性更好，让实现变的更简单，为了从 MyBatis 根本解决问题，给官方提过好几个 PR，在 2019年3月份给 MyBatis 提交的
[pr#1391](https://github.com/mybatis/mybatis-3/pull/1391) 合并后（对应 3.5.1 版本，最低要求版本），终于能以更简单的方式来实现通用 Mapper 了。

由于此次变动太大，因此不打算对 **通用Mapper4** 进行任何改动，从头实现了一个新的项目，名字仍然没有新意的使用了 `mybatis-mapper`，推荐在新项目中使用：

- [GitHub - https://github.com/mybatis-mapper/mapper](https://github.com/mybatis-mapper/mapper)
- [Gitee - https://gitee.com/mybatis-mapper/mapper](https://gitee.com/mybatis-mapper/mapper)
- [文档: https://mapper.mybatis.io](https://mapper.mybatis.io/)
- [开发过程 - https://mapper.mybatis.io/releases/1.0.0.html](https://mapper.mybatis.io/releases/1.0.0.html)
- [快速上手 - https://mapper.mybatis.io/docs/1.getting-started.html#%E4%BB%8B%E7%BB%8D](https://mapper.mybatis.io/docs/1.getting-started.html#%E4%BB%8B%E7%BB%8D)

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

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

## 《MyBatis 从入门到精通》

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
