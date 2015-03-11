可能有些人也有过类似需求，一般都会选择使用其他的方式如Spring-JDBC等方式解决。

能否通过MyBatis实现这样的功能呢？

为了让通用Mapper更彻底的支持多表操作以及更灵活的操作，在<b>2.2.0版本</b>增加了一个可以直接执行SQL的新类`SqlMapper`。

通过这篇博客，我们来了解一下[`SqlMapper`](http://git.oschina.net/free/Mapper/blob/master/src/main/java/com/github/abel533/sql/SqlMapper.java)。

##`SqlMapper`提供的方法

`SqlMapper`提供了以下这些公共方法：

- `Map<String,Object> selectOne(String sql)`

- `Map<String,Object> selectOne(String sql, Object value)`

- `<T> T selectOne(String sql, Class<T> resultType)`

- `<T> T selectOne(String sql, Object value, Class<T> resultType)`

- `List<Map<String,Object>> selectList(String sql)`

- `List<Map<String,Object>> selectList(String sql, Object value)`

- `<T> List<T> selectList(String sql, Class<T> resultType)`

- `<T> List<T> selectList(String sql, Object value, Class<T> resultType)`

- `int insert(String sql)`

- `int insert(String sql, Object value)`

- `int update(String sql)`

- `int update(String sql, Object value)`

- `int delete(String sql)`

- `int delete(String sql, Object value)`

一共14个方法，这些方法的命名和参数和`SqlSession`接口的很像，只是基本上第一个参数都成了sql。

其中`Object value`为入参，入参形式和`SqlSession`中的入参一样，带有入参的方法，在使用时sql可以包含`#{param}`或`${param}`形式的参数，这些参数需要通过入参来传值。需要的参数过多的时候，参数可以使用`Map`类型。另外这种情况下的sql还支持下面这种复杂形式：
```java
String sql = "<script>select * from sys_user where 1=1"  + 
        "<if test=\"usertype != null\">usertype = #{usertype}</if></script>";
```
这种情况用的比较少，不多说。

不带有`Object value`的所有方法，sql中如果有参数需要手动拼接成一个可以直接执行的sql语句。

在`selectXXX`方法中，使用`Class<T> resultType`可以指定返回类型，否则就是`Map<String,Object>`类型。

##实例化`SqlMapper`

`SqlMapper`构造参数`public SqlMapper(SqlSession sqlSession)`，需要一个入参`SqlSession sqlSession`，在一般系统中，可以按照下面的方式获取：

```java
SqlSession sqlSession = (...);//通过某些方法获取sqlSession
//创建sqlMapper
SqlMapper sqlMapper = new SqlMapper(sqlSession);
```

如果使用的Spring，那么可以按照下面的方式配置`<bean>`:
```xml
<bean id="sqlMapper" class="com.github.abel533.sql.SqlMapper" scope="prototype">
  <constructor-arg ref="sqlSession"/>
</bean>
```

在Service中使用的时候可以直接使用`@Autowired`注入。

<b>注意：</b>`SqlMapper`增加了一个构造方法`public SqlMapper(SqlSession sqlSession, boolean cached)`，可以通过`cache`控制是否缓存动态创建的`MappedStatement`。

使用`public SqlMapper(SqlSession sqlSession)`时，`cached`默认为`true`，会缓存。

###如何选择是否缓存动态创建的`MappedStatement`?

1. 当通过前台实现执行任意SQL时，建议关闭缓存，否则每一个SQL缓存一次，最终的缓存数量会很大

2. 如果是业务代码中拼的sql，建议缓存提高效率

##简单例子

在`src/test/java`目录的`com.github.abel533.sql`包中包含这些方法的测试。

下面挑几个看看如何使用。

###`selectList`

```java
//查询，返回List<Map>
List<Map<String, Object>> list = sqlMapper.selectList("select * from country where id < 11");

//查询，返回指定的实体类
List<Country> countryList = sqlMapper.selectList("select * from country where id < 11", Country.class);

//查询，带参数
countryList = sqlMapper.selectList("select * from country where id < #{id}", 11, Country.class);

//复杂点的查询，这里参数和上面不同的地方，在于传入了一个对象
Country country = new Country();
country.setId(11);
countryList = sqlMapper.selectList("<script>" +
        "select * from country " +
        "   <where>" +
        "       <if test=\"id != null\">" +
        "           id &lt; #{id}" +
        "       </if>" +
        "   </where>" +
        "</script>", country, Country.class);
```

###`selectOne`

```java
Map<String, Object> map = sqlMapper.selectOne("select * from country where id = 35");

map = sqlMapper.selectOne("select * from country where id = #{id}", 35);

Country country = sqlMapper.selectOne("select * from country where id = 35", Country.class);

country = sqlMapper.selectOne("select * from country where id = #{id}", 35, Country.class);
```

###`insert,update,delete`

```java
//insert
int result = sqlMapper.insert("insert into country values(1921,'天朝','TC')");

Country tc = new Country();
tc.setId(1921);
tc.setCountryname("天朝");
tc.setCountrycode("TC");
//注意这里的countrycode和countryname故意写反的
result = sqlMapper.insert("insert into country values(#{id},#{countrycode},#{countryname})"
                          , tc);


//update
result = sqlMapper.update("update country set countryname = '天朝' where id = 35");

tc = new Country();
tc.setId(35);
tc.setCountryname("天朝");

int result = sqlMapper.update("update country set countryname = #{countryname}" + 
           " where id in(select id from country where countryname like 'A%')", tc);


//delete
result = sqlMapper.delete("delete from country where id = 35");
result = sqlMapper.delete("delete from country where id = #{id}", 35);

```

###注意

通过上面这些例子应该能对此有个基本的了解，但是如果你使用参数方式，建议阅读下面的文章：

>[深入了解MyBatis参数](http://blog.csdn.net/isea533/article/details/44002219)


##实现原理

2015-03-09：最初想要设计这个功能的时候，感觉会很复杂，想的也复杂，需要很多个类，因此当时没有实现。

2015-03-10：突发奇想，设计了现在的这种方式。并且有种强烈的感觉就是幸好昨天没有尝试去实现，因为昨天晚上思考这个问题的时候是晚上10点多，而今天（10号）是7点开始思考。我很庆幸在一个更清醒的状态下去写这段代码。

下面简单说思路和实现方式。

在写[MyBatis分页插件](http://git.oschina.net/free/Mybatis_PageHelper)的时候熟悉了`MappedStatement`类。

在写[通用Mapper](http://git.oschina.net/free/Mapper)的时候熟悉了`xml`转`SqlNode`结构。

如果我根据SQL动态的创建一个`MappedStatement`，然后使用`MappedStatement`的`id`在`sqlSession`中执行不就可以了吗？

想到这一点，一切就简单了。

看看下面select查询创建`MappedStatement`的代码：

```java
/**
 * 创建一个查询的MS
 *
 * @param msId
 * @param sqlSource 执行的sqlSource
 * @param resultType 返回的结果类型
 */
private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
    MappedStatement ms = new MappedStatement.Builder(
            configuration, msId, sqlSource, SqlCommandType.SELECT)
        .resultMaps(new ArrayList<ResultMap>() {
            {
                add(new ResultMap.Builder(configuration,
                        "defaultResultMap",
                        resultType,
                        new ArrayList<ResultMapping>(0)).build());
            }
        })
        .build();
    //缓存
    configuration.addMappedStatement(ms);
}
```

代码是不是很简单，这段代码的关键是参数`sqlSource`，下面是创建`SqlSource`的方法，分为两种。

一种是一个完整的sql，不需要参数的，可以直接执行的：

```java
StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
```
其中`configuration`从`sqlSession`中获取，`sql`就是用户传入到sql语句，是不是也很简单？

另一种是支持动态sql的，支持参数的`SqlSource`：

```java
SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
```
是不是也很简单？这个方法其实可以兼容上面的`StaticSqlSource`，这里比上面多了一个`parameterType`，因为这儿是可以传递参数的，另外`languageDriver`是从`configuration`中获取的。

是不是很简单？

我一开始也没想到MyBatis直接执行sql实现起来会这么的容易。

`insert,delete,update`方法的创建更容易，因为他们的返回值都是`int`，所以处理起来更简单，有兴趣的可以去[通用Mapper](http://git.oschina.net/free/Mapper)下的包`com.github.abel533.sql`中查看`SqlMapper`的源码。