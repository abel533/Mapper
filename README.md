#Mybatis通用Mapper

##  ------极其方便的使用Mybatis基础的增删改查

##优点?

不客气的说,使用这个通用Mapper甚至能改变你对Mybatis基础操作不方便的想法,使用它你能简单的使用增删改查,包含动态的增删改查.

程序使用拦截器实现具体的执行Sql,完全使用原生的Mybatis进行操作.

继续往下看如何使用吧,相信这个通用的Mapper会让你更方便的使用Mybatis,这是一个强大的Mapper!

不管你信不信,这个项目的测试代码中没有一个Mapper的xml配置文件,但是却可以做到

发现BUG可以提Issue,可以给我发邮件,可以加我QQ,可以进Mybatis群讨论(群和我无关,我有时会帮忙解答一些问题).

作者博客：http://blog.csdn.net/isea533

作者QQ： 120807756

作者邮箱： abel533@gmail.com

推荐一个Mybatis的QQ群： 146127540

推荐使用Mybatis分页插件:[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)

##如何使用?

下面是基于Mybatis配置的配置方法,稍后会增加Spring集成的配置方法.还会讲和[PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)集成的配置方式.

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

###2. 配置Mapper拦截器(后续会提供Spring集成的配置)

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
    <!--主键自增回写方法,默认值CALL IDENTITY(),适应于大多数数据库-->
    <!--<property name="IDENTITY" value="CALL IDENTITY()"/>-->
    <!--主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)-->
    <!--<property name="ORDER" value="AFTER"/>-->
  </plugin>
</plugins>
```
可配置参数一般情况下不需要修改,直接像下面这样一行即可:
```xml
<plugin interceptor="com.github.abel533.mapper.MapperInterceptor"></plugin>
```

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

1. 表名默认使用类名,驼峰转下划线,如`UserInfo`默认对应的表名为`user_info`.

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

2. 使用UUID时:
```java
//可以用于任意字符串类型长度超过32位的字段
@GeneratedValue(generator = "UUID")
private String countryname;
```

3. 使用主键自增:
```java
//不限于@Id注解的字段,但是一个实体类中只能存在一个(继承关系中也只能存在一个)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
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

##当前版本v0.1.0

##这是一个刚刚出生的开源项目

首先感谢您能看到这里!

这是一个新生的项目,一切都刚刚开始,虽然项目中包含大量的测试,但是仍然会有很多未知的bug存在,希望各位能够在使用过程中发现问题时及时反馈,欢迎各位fork本项目进行参与!

请相信,我一定会做好这个项目.

因为本项目使用原生的Mybatis进行设计,所以如果有可能,我会对项目重构以便于本项目能够直接加入Mybatis正式项目中.使开发人员更方便的使用.