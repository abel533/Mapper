#Mybatis通用Mapper

##极其方便的使用Mybatis单表的增删改查

##优点?

不客气的说,使用这个通用Mapper甚至能改变你对Mybatis单表基础操作不方便的想法,使用它你能简单的使用单表的增删改查,包含动态的增删改查.

程序使用拦截器实现具体的执行Sql,完全使用原生的Mybatis进行操作.

你还在因为数据库表变动重新生成xml吗?还是要手动修改自动生成的insert|update|delete的xml呢?赶紧使用通用Mapper,表的变动只需要实体类保持一致,不用管基础的xml,你不止会拥有更多的时间陪老婆|孩子|女朋友|打DOTA,你也不用做哪些繁琐无聊的事情,感兴趣了吗?继续看如何使用吧!!相信这个通用的Mapper会让你更方便的使用Mybatis,这是一个强大的Mapper!!!

不管你信不信,这个项目的测试代码中没有一个Mapper的xml配置文件,但是却可以做到每个Mapper对应上百行xml才能完成的许多功能.没有了这些基础xml信息的干扰,你将会拥有清晰干净的Mapper.xml.

发现BUG可以提Issue,可以给我发邮件,可以加我QQ,可以进Mybatis群讨论.

作者博客：http://blog.csdn.net/isea533

作者QQ： 120807756

作者邮箱： abel533@gmail.com

Mybatis工具群： 211286137 (Mybatis相关工具插件等等)

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

##v0.2.0版本说明

该版本做了大量的重构，在原有基础上增加了两个类，分别为`MapperTemplate`和`MapperProvider`，其他几个类都有相当大的改动。  

**但是**，这次重构并不影响原有的业务代码。  

这次重构的目的是为了方便开发者自行扩展，增加自己需要的通用Mapper。这次重构后，扩展变的更容易。稍后会写一篇**如何进行扩展**的文档。  

这次更新还修复Oracle序列的BUG。

##[如何开发自己的通用Mapper](http://git.oschina.net/free/Mapper/blob/master/HowToExtendMapper.md)  

##如何使用?

下面是通用Mapper的配置方法,还会提到Spring中的配置方法.还有和[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)集成的配置方式.

###1. 引入通用Mapper的代码

将本项目中的4个代码文件(`EntityHelper`,`Mapper`,`MapperHelper`,`MapperInterceptor`)复制到你自己的项目中.

项目依赖于JPA的注解,需要引入`persistence-api-1.0.jar`或者添加Maven依赖:
```xml
<dependency>
  <groupId>javax.persistence</groupId>
  <artifactId>persistence-api</artifactId>
  <version>1.0</version>
</dependency>
```

###2. 配置Mapper拦截器

在`mybatis-config.xml`中添加如下配置:
```xml
<plugins>
  <plugin interceptor="com.github.abel533.mapper.MapperInterceptor">
    <!--================================================-->
    <!--可配置参数说明(一般无需修改)-->
    <!--================================================-->
    <!--UUID生成策略-->
    <!--配置UUID生成策略需要使用OGNL表达式-->
    <!--默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")-->
    <!--<property name="UUID" value="@java.util.UUID@randomUUID().toString()"/>-->
    <!--主键自增回写方法,默认值MYSQL,详细说明请看文档-->
    <property name="IDENTITY" value="HSQLDB"/>
    <!--序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle-->
    <!--可选参数一共3个，对应0,1,2,分别为SequenceName，ColumnName,PropertyName-->
    <property name="seqFormat" value="{0}.nextval"/>
    <!--主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)-->
    <!--<property name="ORDER" value="AFTER"/>-->
    <!--通用Mapper接口，多个通用接口用逗号隔开-->
    <property name="mappers" value="com.github.abel533.mapper.Mapper"/>
  </plugin>
</plugins>
```

**mappers**参数：这里先记住是通用Mapper的全限定路径即可。后面讲如何扩展时，会详细介绍。

####INENTITY参数配置

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

为了配置的时候方便，可以直接使用这里的数据库名字进行配置，例如:
```xml
<plugins>
  <plugin interceptor="com.github.abel533.mapper.MapperInterceptor">
    <property name="IDENTITY" value="DB2"/>
  </plugin>
</plugins>
```
如果这里的值不是上面所提到的数据库，就会使用直接提供的语句。例如下面的这个配置和上面的效果一样：
```xml
<plugins>
  <plugin interceptor="com.github.abel533.mapper.MapperInterceptor">
    <property name="IDENTITY" value="VALUES IDENTITY_VAL_LOCAL()"/>
  </plugin>
</plugins>
```  

<b>附:Spring配置相关</b>

如果你使用Spring的方式来配置该拦截器,你可以像下面这样:
```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource"/>
  <property name="mapperLocations">
    <array>
      <value>classpath:mapper/*.xml</value>
    </array>
  </property>
  <property name="typeAliasesPackage" value="com.isea533.mybatis.model"/>
  <property name="plugins">
    <array>
      <-- 主要看这里 -->
      <bean class="com.isea533.mybatis.mapperhelper.MapperInterceptor"/>
    </array>
  </property>
</bean>
```
只需要像上面这样配置一个bean即可.

如果你同时使用了[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper),可以像下面这样配置:
```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource"/>
  <property name="mapperLocations">
    <array>
      <value>classpath:mapper/*.xml</value>
    </array>
  </property>
  <property name="typeAliasesPackage" value="com.isea533.mybatis.model"/>
  <property name="plugins">
    <array>
      <bean class="com.isea533.mybatis.pagehelper.PageHelper">
        <property name="properties">
          <value>
            dialect=hsqldb
            reasonable=true
          </value>
        </property>
      </bean>
      <bean class="com.isea533.mybatis.mapperhelper.MapperInterceptor"/>
    </array>
  </property>
</bean>
```
一定要注意`PageHelper`和`MapperInterceptor`这两者的顺序不能颠倒.

如果你想配置`MapperInterceptor`的参数,可以像`PageHelper`中的`properties`参数这样配置


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
该字段不会回写。   

2. 使用UUID时:
```java
//可以用于任意字符串类型长度超过32位的字段
@GeneratedValue(generator = "UUID")
private String countryname;
```
该字段不会回写。 

3. 使用主键自增:
```java
//不限于@Id注解的字段,但是一个实体类中只能存在一个(继承关系中也只能存在一个)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
```  
增加这个注解后，**会回写ID**。

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

##当前版本v0.2.0

##这是一个新生的开源项目

首先感谢您能看到这里!

这是一个新生的项目,一切都刚刚开始,虽然项目中包含大量的测试,但是仍然会有很多未知的bug存在,希望各位能够在使用过程中发现问题时及时反馈,欢迎各位fork本项目进行参与!