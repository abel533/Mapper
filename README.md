# MyBatis 通用 Mapper6 来了🎉🎉🎉

[![Maven Central](https://img.shields.io/maven-central/v/tk.mybatis/mapper)](https://central.sonatype.com/namespace/tk.mybatis)

通用Mapper都可以极大的方便开发人员。可以随意的按照自己的需要选择通用方法，还可以很方便的开发自己的通用方法。

极其方便的使用MyBatis单表的增删改查。

支持单表操作，不支持通用的多表联合查询。

## 🌟 赞助支持

本项目是基于MIT协议的开源项目，无任何商业盈利，**持续的维护、迭代与兼容升级全靠各位开发者和赞助者的支持**。

[![Easysearch - ES 国产化的首选替代方案](https://cdn.nlark.com/yuque/0/2026/png/775247/1774234461319-11984f50-5516-4587-b82d-7ed91cb64930.png)](https://easysearch.cn/)

## 版本匹配说明

不同分支支持不同的 Spring Boot 版本，请根据项目实际情况选择对应分支：

| 分支 | Spring Boot 版本 | JDK 版本 | Mapper 版本 |
|------|------------------|----------|-------------|
| master | Spring Boot 4.x | JDK 17+ | 6.0.0+ |
| 5.x | Spring Boot 3.x | JDK 17+ | 5.0.0 |
| 4.3.x | Spring Boot 2.x | JDK 8+ | 4.3.x |

## 基于 JDK 17 + Jakarta JPA 注解 + Spring Boot 4

配置完全兼容，需要使用新版本的 JPA 注解，同步更新（copy） mybatis-spring 4.0.0 和 mybatis-spring-boot-starter 4.0.0。

此次更新主要是依赖的更新，适配最新的 Spring Boot 4.0.2，提供更好的兼容性和稳定性。

```xml
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper</artifactId>
    <version>6.0.0</version>
</dependency>
```

## 推荐新版本 mybatis-mapper

如果你要在新项目中使用，可以看看新版本的 mybatis-mapper，完全作为 mybatis 扩展存在，
不修改 mybatis, mybatis-spring, mybatis-spring-boot-starter 任何代码，不需要额外配置，可以快速上手。

- mybatis-mapper: https://github.com/mybatis-mapper/mapper
- mybatis-mapper 文档: https://mapper.mybatis.io
- [mybatis-mapper 快速入门](https://mapper.mybatis.io/docs/1.getting-started.html#%E4%BB%8B%E7%BB%8D)

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

## 微信公众号

<img src="wx-mybatis.webp" width="250"/>

## 作者信息

MyBatis 工具网站：[https://mybatis.io](https://mybatis.io)

作者博客：http://blog.csdn.net/isea533 ，http://blog.mybatis.io

作者邮箱：abel533@gmail.com

推荐使用Mybatis分页插件：[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

## 《MyBatis 从入门到精通》

![MyBatis 从入门到精通](https://github.com/mybatis-book/book/raw/master/book.png)

### 简介

本书中从一个简单的 MyBatis 查询入手，搭建起学习 MyBatis 的基础开发环境。 通过全面的示例代码和测试讲解了在 MyBatis XML 方式和注解方式中进行增、删、改、查操作的基本用法，介绍了动态 SQL
在不同方面的应用以及在使用过程中的最佳实践方案。 针对 MyBatis 高级映射、存储过程和类型处理器提供了丰富的示例，通过自下而上的方法使读者更好地理解和掌握MyBatis 的高级用法，同时针对 MyBatis
的代码生成器提供了详细的配置介绍。 此外，本书还提供了缓存配置、插件开发、Spring、Spring Boot 集成的详细内容。 最后通过介绍 Git 和 GitHub 让读者了解MyBatis 开源项目，通过对 MyBatis
源码和测试用例的讲解让读者更好掌握 MyBatis。

### 购买地址：

- [京东](https://item.jd.com/12103309.html)

### 相关介绍

- CSDN博客：http://blog.csdn.net/isea533/article/details/73555400

- GitHub项目：https://github.com/mybatis-book/book
