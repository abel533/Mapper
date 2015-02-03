#通用Mapper的代码

将本项目中的代码文件复制到你自己的项目中，代码文件如下：

* `com.github.abel533.generator`包下面的是通用Mapper的MyBatis Generator插件

   * `MapperCommentGenerator`:注释以及字段注解处理类。

   * `MapperPlugin`:处理主要逻辑，详细信息请看 [如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/UseMBGInMapper.md)

* `com.github.abel533.mapperhelper`包下面的是通用Mapper的关键类

   * `EntityHelper`:实体类工具类 - 处理实体和数据库表以及字段关键的一个类

   * `MapperHelper`:处理主要逻辑，最关键的一个类

   * `MapperTemplate`:通用Mapper模板类，扩展通用Mapper时需要继承该类

* `com.github.abel533.mapper`包下面是通用Mapper自带的一个默认实现

   * `Mapper`:通用Mapper接口类

   * `MapperProvider`:通用Mapper接口类对应的实现类

##EntityMapper

包`com.github.abel533.entity`
