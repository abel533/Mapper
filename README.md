#Mybatis通用Mapper

##极其方便的使用Mybatis单表的增删改查

##优点?

使用它你能简单的使用单表的增删改查,包含动态的增删改查,你还可以参考文档[开发自己的通用Mapper](http://git.oschina.net/free/Mapper/blob/master/HowToExtendMapper.md)。  

如果你使用Spring4集成，可以使用泛型注入，具体方法请看[在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md)。  

本插件在项目启动时根据实体动态生成实际执行的Sql,完全使用原生的Mybatis进行操作，不存在效率问题，和自己写的xml一样的效果。  

本插件还提供了通用Mapper专用的MyBatis Generator插件，使用方法可以看[如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/UseMBGInMapper.md)

**通用Mapper支持Mybatis-3.2.4及以上版本**  

发现BUG可以提Issue,可以给我发邮件,可以加我QQ,可以进Mybatis群讨论.

作者博客：http://blog.csdn.net/isea533

作者QQ： 120807756

作者邮箱： abel533@gmail.com

Mybatis工具群： 211286137 (Mybatis相关工具插件等等)

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

##独立文档

###1. [如何开发自己的通用Mapper](http://git.oschina.net/free/Mapper/blob/master/HowToExtendMapper.md)  

###2. [在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md)  

###3. [如何使用Mapper专用的MyBatis Generator插件](http://git.oschina.net/free/Mapper/blob/master/UseMBGInMapper.md)

##最新版V1.1.0

* 完善文档
* 解决主键selectKey的一个bug
* 解决@Column注解为空时的bug
* 完善自动增长的配置，增加对JDBC的支持`@GeneratedValue(generator = "JDBC")`,详细请看下面关于主键策略的详细内容
* 增加了一个`notEmpty`参数，该参数会影响所有使用`getAllIfColumnNode`方法的地方,具体到`Mapper<T>`,影响3个方法：select,selectCount,delete。如果设置为`true`，那么`<if ...`的条件中会包含`String`类型`property!=''`的条件。

##如何使用?

下面是通用Mapper的配置方法。

###1. 引入通用Mapper的代码

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

或者使用Maven引入依赖：  

```xml
<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>mapper</artifactId>
    <!-- 1.1.0是版本号，推荐使用最新版 -->
    <version>1.1.0</version>
</dependency>
```

项目依赖于JPA的注解,需要引入`persistence-api-1.0.jar`或者添加Maven依赖:
```xml
<dependency>
  <groupId>javax.persistence</groupId>
  <artifactId>persistence-api</artifactId>
  <version>1.0</version>
</dependency>
```

###2. 配置MapperHelper

首先本项目中已经移除了拦截器，不在使用拦截器的配置方式。增加了两种更方便的使用方式。

**第一种、Java编码**

对于单独使用Mybatis，通过如下方式创建`sqlSessionFactory`:
```java
Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
reader.close();
```  

使用直接的JAVA编码方式，可以在初始化`sqlSessionFactory`的地方按照下面的方式操作：  
```java
//从上面的sqlSessionFactory取出一个session
session = sqlSessionFactory.openSession();
//创建一个MapperHelper
MapperHelper mapperHelper = new MapperHelper();
// 设置UUID生成策略
// 配置UUID生成策略需要使用OGNL表达式
// 默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
mapperHelper.setUUID("");
// 主键自增回写方法,默认值MYSQL,详细说明请看文档
mapperHelper.setIDENTITY("HSQLDB");
// 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
// 可选参数一共3个，对应0,1,2,分别为SequenceName，ColumnName, PropertyName
mapperHelper.setSeqFormat("NEXT VALUE FOR {0}");
// 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
mapperHelper.setCatalog("");
// 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
// 如果同时设置了catalog,优先使用catalog.tablename
mapperHelper.setSchema("");
// 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
mapperHelper.setOrder("AFTER");
// 注册通用Mapper接口
mapperHelper.registerMapper(Mapper.class);
mapperHelper.registerMapper(HsqldbMapper.class);
//配置完成后，执行下面的操作
mapperHelper.processConfiguration(session.getConfiguration());
//OK - mapperHelper的任务已经完成，可以不管了
```
上面配置参数的时候，是一个个调用set方法进行的，你还可以使用`MapperHelper`的`MapperHelper(Properties properties)`构造方法，或者调用`setProperties(properties)`方法，通过`.properties`配置文件来配置。

如果你的情况适用于这种方式，推荐你用JAVA编码的方式处理。  

**第二种、还有一种最常见的情况，那就是和Spring集成的情况**

在Spring中使用的时候，可以通过xml达到上面Java编码方式的效果。如下配置：  
```xml
<bean class="com.github.abel533.mapperhelper.MapperHelper"
        depends-on="sqlSession" init-method="initMapper" scope="singleton" lazy-init="false">
    <property name="mappers">
        <array>
            <!-- 可以配置多个 -->
            <value>com.isea533.mybatis.mapper.Mapper</value>
        </array>
    </property>
    <!-- 对于多数据源，这里也可以像上面这样配置多个 -->
    <property name="sqlSessions" ref="sqlSession"/>
</bean>
```  
可以看到配置中依赖了`sqlSession`，所以使用这种方式，你还要在Spring的配置中保证`sqlSession`存在。一般情况下都会在Spring定义`sqlSession`。一般的定义方法如下：  
```xml
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate" scope="prototype">
    <constructor-arg index="0" ref="sqlSessionFactory"/>
</bean>
```  
在Spring中使用这种方式的时候，Spring启动完成的时候，所有的通用Mapper都已经处理完成了。后面就可以直接使用通用方法，不需要拦截器来执行了。

####INENTITY参数配置（仅对 insert 有用）

对于不同的数据库，需要配置不同的参数，这些参数如下：
  
- <b>DB2</b>: `VALUES IDENTITY_VAL_LOCAL()`  
- <b>MYSQL</b>: `SELECT LAST_INSERT_ID()`  
- <b>SQLSERVER</b>: `SELECT SCOPE_IDENTITY()`  
- <b>CLOUDSCAPE</b>: `VALUES IDENTITY_VAL_LOCAL()`  
- <b>DERBY</b>: `VALUES IDENTITY_VAL_LOCAL()`  
- <b>HSQLDB</b>: `CALL IDENTITY()`  
- <b>SYBASE</b>: `SELECT @@IDENTITY`  
- <b>DB2_MF</b>: `SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1`  
- <b>INFORMIX</b>: `select dbinfo('sqlca.sqlerrd1') from systables where tabid=1`
- <b>JDBC</b>:这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段）。

JAVA编码方式使用：

	// 主键自增回写方法,默认值MYSQL,详细说明请看文档
	mapperHelper.setIDENTITY("HSQLDB");

Spring中可以属性注入：

	<property name="IDENTITY" value="MYSQL"/>

###3. 继承通用的`Mapper<T>`,必须指定泛型`<T>`

例如下面的例子:
```java
public interface UserInfoMapper extends Mapper<UserInfo> {
  //其他必须手写的接口...

}
```
一旦继承了`Mapper<T>`,继承的`Mapper`就拥有了以下通用的方法:
```java
//根据实体类不为null的字段进行查询,条件全部使用=号and条件
List<T> select(T record);

//根据实体类不为null的字段查询总数,条件全部使用=号and条件
int selectCount(T record);

//根据主键进行查询,必须保证结果唯一
//单个字段做主键时,可以直接写主键的值
//联合主键时,key可以是实体类,也可以是Map
T selectByPrimaryKey(Object key);

//插入一条数据
//支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
//优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
int insert(T record);

//插入一条数据,只插入不为null的字段,不会影响有默认值的字段
//支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
//优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
int insertSelective(T record);

//根据实体类中字段不为null的条件进行删除,条件全部使用=号and条件
int delete(T key);

//通过主键进行删除,这里最多只会删除一条数据
//单个字段做主键时,可以直接写主键的值
//联合主键时,key可以是实体类,也可以是Map
int deleteByPrimaryKey(Object key);

//根据主键进行更新,这里最多只会更新一条数据
//参数为实体类
int updateByPrimaryKey(T record);

//根据主键进行更新
//只会更新不是null的数据
int updateByPrimaryKeySelective(T record);
```

###4. 泛型(实体类)`<T>`的类型必须符合要求

实体类按照如下规则和数据库表进行转换,注解全部是JPA中的注解:

1. 表名默认使用类名,驼峰转下划线(只对大写字母进行处理),如`UserInfo`默认对应的表名为`user_info`。

2. 表名可以使用`@Table(name = "tableName")`进行指定,对不符合第一条默认规则的可以通过这种方式指定表名.

3. 字段默认和`@Column`一样,都会作为表字段,表字段默认为Java对象的`Field`名字驼峰转下划线形式.

4. 可以使用`@Column(name = "fieldName")`指定不符合第3条规则的字段名

5. 使用`@Transient`注解可以忽略字段,添加该注解的字段不会作为表字段使用.

6. 建议一定是有一个`@Id`注解作为主键的字段,可以有多个`@Id`注解的字段作为联合主键.

7. 默认情况下,实体类中如果不存在包含`@Id`注解的字段,所有的字段都会作为主键字段进行使用(这种效率极低).

8. 实体类可以继承使用,可以参考测试代码中的`com.github.abel533.model.UserLogin2`类.

9. 由于基本类型,如int作为实体类字段时会有默认值0,而且无法消除,所以实体类中建议不要使用基本类型.

除了上面提到的这些,Mapper还提供了序列(支持Oracle)、UUID(任意数据库,字段长度32)、主键自增(类似Mysql,Hsqldb)三种方式，其中序列和UUID可以配置多个，主键自增只能配置一个。

这三种方式不能同时使用,同时存在时按照 `序列>UUID>主键自增`的优先级进行选择.下面是具体配置方法:

1. 使用序列可以添加如下的注解:
```java
//可以用于数字类型,字符串类型(需数据库支持自动转型)的字段
@SequenceGenerator(name="Any",sequenceName="seq_userid")
@Id
private Integer id;
```
该字段不会回写。这种情况对应类似如下的XML：
  ```xml
  <insert id="insertAuthor">
      insert into Author
        (id, username, password, email,bio, favourite_section)
      values
        (seq_userid.nextval, #{username, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
  </insert>
  ```   

2. 使用UUID时:
```java
//可以用于任意字符串类型长度超过32位的字段
@GeneratedValue(generator = "UUID")
private String username;
```
该字段不会回写。这种情况对应类似如下的XML：
  ```xml
  <insert id="insertAuthor">
      <bind name="username_bind" value='@java.util.UUID@randomUUID().toString().replace("-", "")' />
      insert into Author
        (id, username, password, email,bio, favourite_section)
      values
        (#{id}, #{username_bind}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
  </insert>
  ```

3. 使用主键自增:
```java
//不限于@Id注解的字段,但是一个实体类中只能存在一个(继承关系中也只能存在一个)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
```  
增加这个注解后，__会回写ID__。

  通过设置`@GeneratedValue`的`generator`参数可以支持更多的获取主键的方法，例如在Oracle中使用序列：
  ```java
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select SEQ_ID.nextval from dual")
  private Integer id;
  ```  
  使用Oracle序列的时候，还需要配置:
  ```xml
  <property name="ORDER" value="BEFORE"/>
  ```
  因为在插入数据库前，需要先获取到序列值，否则会报错。  
  这种情况对于的xml类似下面这样：  
  ```xml
  <insert id="insertAuthor">
    <selectKey keyProperty="id" resultType="int" order="BEFORE">
      select SEQ_ID.nextval from dual
    </selectKey>
    insert into Author
      (id, username, password, email,bio, favourite_section)
    values
      (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
  </insert>
  ```

4. 主键自增还有一种简单的写法：  
```java
//不限于@Id注解的字段,但是一个实体类中只能存在一个(继承关系中也只能存在一个)
@GeneratedValue(generator = "JDBC")
private Integer id;
```
  这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段）。
  这种情况对应的xml类似下面这样:
  ```xml
  <insert id="insertAuthor" useGeneratedKeys="true" keyProperty="id">
    insert into Author (username,password,email,bio)
    values (#{username},#{password},#{email},#{bio})
  </insert>
  ```



###5. 将继承的Mapper接口添加到Mybatis配置中

例如本项目测试中的配置:
```xml
<mappers>
  <mapper class="com.github.abel533.mapper.CountryMapper" />
  <mapper class="com.github.abel533.mapper.Country2Mapper" />
  <mapper class="com.github.abel533.mapper.CountryTMapper" />
  <mapper class="com.github.abel533.mapper.CountryUMapper" />
  <mapper class="com.github.abel533.mapper.CountryIMapper" />
  <mapper class="com.github.abel533.mapper.UserInfoMapper" />
  <mapper class="com.github.abel533.mapper.UserLoginMapper" />
  <mapper class="com.github.abel533.mapper.UserLogin2Mapper" />
</mappers>
```

<b>附:Spring配置相关</b>

如果你在Spring中配置Mapper接口,不需要像上面这样一个个配置,只需要有下面的这个扫描Mapper接口的这个配置即可:
```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  <property name="basePackage" value="com.isea533.mybatis.mapper"/>
</bean>
```  

如果想在Spring4中使用泛型注入，还需要包含`Mapper<T>`所在的包，具体请看 [在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md)。  

###6. 代码中使用

例如下面这个简单的例子:
```java
SqlSession sqlSession = MybatisHelper.getSqlSession();
try {
    //获取Mapper
    UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("abel533");
    userInfo.setPassword("123456");
    userInfo.setUsertype("2");
    userInfo.setEmail("abel533@gmail.com");
    //新增一条数据
    Assert.assertEquals(1, mapper.insert(userInfo));
    //ID回写,不为空
    Assert.assertNotNull(userInfo.getId());
    //6是当前的ID
    Assert.assertEquals(6, (int)userInfo.getId());
    //通过主键删除新增的数据
    Assert.assertEquals(1,mapper.deleteByPrimaryKey(userInfo));
} finally {
    sqlSession.close();
}
```

另一个例子:
```java
SqlSession sqlSession = MybatisHelper.getSqlSession();
try {
    //获取Mapper
    CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
    //查询总数
    Assert.assertEquals(183, mapper.selectCount(new Country()));
    //查询100
    Country country = mapper.selectByPrimaryKey(100);
    //根据主键删除
    Assert.assertEquals(1, mapper.deleteByPrimaryKey(100));
    //查询总数
    Assert.assertEquals(182, mapper.selectCount(new Country()));
    //插入
    Assert.assertEquals(1, mapper.insert(country));
} finally {
    sqlSession.close();
}
```

<b>附:Spring使用相关</b>

直接在需要的地方注入Mapper继承的接口即可,和一般情况下的使用没有区别.

###7.其他  

如果你的实体是继承Map的，你可能需要将数据库查询的结果从大写下划线形式转换为驼峰形式，你可以搭配下面的拦截器使用：  

**CameHumpInterceptor - Map结果的Key转为驼峰式**  

http://git.oschina.net/free/Mybatis_Utils/tree/master/CameHumpMap  

##更新日志

###v1.0.0正式发布版

* 增加通用Mapper专用的MyBatis Generator插件，可以自动生成实体类注解以及Mapper接口和一个空的xml文件

* 插件后续可能会增加更多的自动生成代码。

* 有关插件的使用，后续完善文档

Maven坐标:

```xml
<dependency>
    <groupId>com.github.abel533</groupId>
    <artifactId>mapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

###v0.3.2版本说明

移除了`MapperInterceptor`类，不在提供拦截器方式的使用。如果有需要可以自己从0.3.1版本获取。
 
另外调整的类的包结构，目的更明确，下面分别介绍这几个类的作用。  

* `com.github.abel533.mapperhelper`包下面的是通用Mapper的关键类

   * `EntityHelper`:实体类工具类 - 处理实体和数据库表以及字段关键的一个类  

   * `MapperHelper`:处理主要逻辑，最关键的一个类  

   * `MapperTemplate`:通用Mapper模板类，扩展通用Mapper时需要继承该类  

* `com.github.abel533.mapper`包下面是通用Mapper自带的一个默认实现

   * `Mapper`:通用Mapper接口类   

   * `MapperProvider`:通用Mapper接口类对应的实现类 

如果你不需要通用Mapper自带的默认实现类`Mapper`，你就不需要`com.github.abel533.mapper`包下面的两个类，你可以根据文档和自己的需求自行扩展。  

###v0.3.1版本说明

支持Spring4泛型注入，详细请看文档[在Spring4中使用通用Mapper](http://git.oschina.net/free/Mapper/blob/master/UseMapperInSpring4.md) 

###v0.3.0版本说明

这个版本的主要目的是消除拦截器，因此针对常用的情况增加了两种更方便的使用方式。


###v0.2.0版本说明

该版本做了大量的重构，在原有基础上增加了两个类，分别为`MapperTemplate`和`MapperProvider`，其他几个类都有相当大的改动。  

**但是**，这次重构并不影响原有的业务代码。  

这次重构的目的是为了方便开发者自行扩展，增加自己需要的通用Mapper。这次重构后，扩展变的更容易。稍后会写一篇**如何进行扩展**的文档。  

这次更新还修复Oracle序列的BUG。