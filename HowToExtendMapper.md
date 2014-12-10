#前言  

自从发了通用Mapper-0.1.0版本后，我觉得对少数人来说，这可能是他们正好需要的一个工具。至少目前的通用DAO中，很少能有比这个更强大的。  

但是对另一部分人来说，使用Mybatis代码生成器（我正在和一些朋友翻译这个文档，地址:[MyBatis Generator](http://generator.sturgeon.mopaas.com/)）生成xml很方便，不需要使用通用Mapper。  

实际上如果你无法在自己的业务中提取出通用的单表（多表实际上能实现，但是限制会增多，不如手写xml）操作，通用的Mapper除了能增加你的初始效率以及更干净的xml配置外，没有特别大的优势。  

为了更方便的扩展通用Mapper，我对0.1.0版本进行了重构。目前已经发布了0.2.0版本，这里要讲如何开发自己需要的通用Mapper。

#如何开发自己的通用Mapper    

##要求  

1. 自己定义的通用Mapper必须包含泛型，例如`MysqlMapper<T>`。

2. 自定义的通用Mapper接口中的方法需要有合适的注解。具体可以参考[`Mapper`](http://git.oschina.net/free/Mapper/blob/master/src/main/java/com/github/abel533/mapper/Mapper.java)

3. 需要继承[`MapperTemplate`](http://git.oschina.net/free/Mapper/blob/master/src/main/java/com/github/abel533/mapper/MapperTemplate.java)来实现具体的操作方法。  

4. 通用Mapper中的`Provider`一类的注解只能使用相同的`type`类型（这个类型就是第三个要实现的类。）。实际上`method`也都写的一样。

##HsqldbMapper实例  

###第一步，创建`HsqldbMapper<T>`  

    public interface HsqldbMapper<T> {
    }

这个接口就是我们定义的通用Mapper，具体的接口方法在**第三步**写。其他的Mapper可以继承这个`HsqldbMapper<T>`。  

###第二部，创建`HsqldbProvider`  

    public class HsqldbProvider extends MapperTemplate {
        //继承父类的方法
        public HsqldbProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }
    }

这个类是实际处理操作的类，需要继承`MapperTemplate`，具体代码在**第四步**写。

###第三步，在`HsqldbMapper<T>`中添加通用方法
这里以一个分页查询作为例子。
    public interface HsqldbMapper<T> {
        /**
         * 单表分页查询
         *
         * @param object
         * @param offset
         * @param limit
         * @return
         */
        @SelectProvider(type=HsqldbProvider.class,method = "dynamicSQL")
        List<T> selectPage(@Param("entity") T object, @Param("offset") int offset, @Param("limit") int limit);
    }

返回结果为List<T>,入参分别为查询条件和分页参数。在Mapper的接口方法中，当有多个入参的时候建议增加`@Param`注解，否则就得用`param1,param2...`来引用参数。  

同时必须在方法上添加注解。查询使用`SelectProvider`，插入使用`@InsertProvider`，更新使用`UpdateProvider`，删除使用`DeleteProvider`。不同的Provider就相当于xml中不同的节点，如`<select>,<insert>,<update>,<delete>`。

因为这里是查询，所以要设置为`SelectProvider`，这4个`Provider`中的参数都一样，只有`type`和`method`。

`type`必须设置为实际执行方法的`HasqldbProvider.class`,`method`必须设置为`"dynamicSQL"`。  

通用Mapper处理的时候会根据type反射`HasqldbProvider`查找方法，而Mybatis的处理机制要求method必须是`type`类中只有一个入参，且返回值为`String`的方法。`"dynamicSQL"`方法定义在`MapperTemplate`中，该方法如下：    

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

这个方法只是为了满足Mybatis的要求，没有任何实际的作用。

###第四步，在`HsqldbProvider`中实现真正处理Sql的方法

在这里有一点要求，那就是`HsqldbProvider`处理`HsqldbMapper<T>`中的方法时，方法名必须一样，因为这里需要通过反射来获取对应的方法，方法名一致一方面是为了减少开发人员的配置，另一方面和接口对应看起来更清晰。  

除了方法名必须一样外，入参必须是`MappedStatement ms`，除此之外返回值可以是`void`或者`SqlNode`之一。  

这里先讲一下通用Mapper的实现原理。通用Mapper目前是通过拦截器在通用方法第一次执行的时候去修改`MappedStatement`对象的`SqlSource`属性。而且只会执行一次，以后就和正常的方法没有任何区别。  

使用`Provider`注解的这个Mapper方法，Mybatis本身会处理成`ProviderSqlSource`(一个`SqlSource`的实现类），由于之前的配置，这个`ProviderSqlSource`种的SQL是上面代码中返回的`"dynamicSQL"`。这个SQL没有任何作用，如果不做任何修改，执行这个代码肯定会出错。所以在拦截器中拦截符合要求的接口方法，遇到`ProviderSqlSource`就通过反射调用如`HsqldbProvider`中的具体代码去修改原有的`SqlSource`。

最简单的处理Mybatis SQL的方法是什么？就是创建`SqlNode`，使用`DynamicSqlSource`，这种情况下我们不需要处理入参，不需要处理代码中的各种类型的参数映射。比执行SQL的方式容易很多。  

有关这部分的内容建议查看通用Mapper的源码和Mybatis源码了解，如果不了解在这儿说多了反而会乱。  

下面在`HsqldbProvider`中添加`public SqlNode selectPage(MappedStatement ms)`方法：

    /**
     * 分页查询
     * @param ms
     * @return
     */
    public SqlNode selectPage(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
    
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + tableName(entityClass)));
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        //对所有列循环，生成<if test="property!=null">[AND] column = #{property}</if>
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode
                    = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn()
							 + " = #{entity." + column.getProperty() + "} ");
            if (column.getJavaType().equals(String.class)) {
                ifNodes.add(new IfSqlNode(columnNode, "entity."+column.getProperty()
							 + " != null and " + "entity."+column.getProperty() + " != '' "));
            } else {
                ifNodes.add(new IfSqlNode(columnNode, "entity."+column.getProperty() + " != null "));
            }
            first = false;
        }
        //将if添加到<where>
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //处理分页
        sqlNodes.add(new IfSqlNode(new StaticTextSqlNode(" LIMIT #{limit}"),"offset==0"));
        sqlNodes.add(new IfSqlNode(new StaticTextSqlNode(" LIMIT #{limit} OFFSET #{offset} "),"offset>0"));
        return new MixedSqlNode(sqlNodes);
    }

**注：**对这段代码感觉吃力的，可以对比本页最下面**结构**部分XML形式的查看。  

首先这段代码要实现的功能是这样，根据传入的实体类参数中不等于null（字符串也不等于'')的属性作为查询条件进行查询，根据分页参数进行分页。   

先看这两行代码：  
	
	//获取实体类型
	Class<?> entityClass = getSelectReturnType(ms);
	//修改返回值类型为实体类型
	setResultType(ms, entityClass);

首先获取了实体类型，然后通过`setResultType`将返回值类型改为entityClass，就相当于`resultType=entityClass`。  

**这里为什么要修改呢？**因为默认返回值是`T`，Java并不会自动处理成我们的实体类，默认情况下是`Object`，对于所有的查询来说，我们都需要手动设置返回值类型。  

对于`insert,update,delete`来说，这些操作的返回值都是`int`，所以不需要修改返回结果类型。  

之后从`List<SqlNode> sqlNodes = new ArrayList<SqlNode>();`代码开始拼写SQL，首先是SELECT查询头，在`EntityHelper.getSelectColumns(entityClass)`中还处理了别名的情况。  

然后获取所有的列，对列循环创建`<if entity.property!=null>column = #{entity.property}</if>`节点。最后把这些if节点组成的List放到一个`<where>`节点中。

这一段使用属性时用的是 `entity. + 属性名`，`entity`来自哪儿？来自我们前面接口定义处的`Param("entity")`注解，后面的两个分页参数也是。如果你用过Mybatis，相信你能明白。  

之后在`<where>`节点后添加分页参数，当`offset==0`时和`offset>0`时的分页代码不同。  

最后封装成一个`MixedSqlNode`返回。  

返回后通用Mapper是怎么处理的，这里贴下源码：  

	SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
	DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
	setSqlSource(ms, dynamicSqlSource);

返回`SqlNode`后创建了`DynamicSqlSource`，然后修改了ms原来的`SqlSource`。  


###第五步，配置通用Mapper接口到拦截器插件中

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
	  <!--支持Map类型的实体类，自动将大写下划线的Key转换为驼峰式-->
	  <!--这个处理使得通用Mapper可以支持Map类型的实体（实体中的字段必须按常规方式定义，否则无法反射获得列）-->
	  <property name="cameHumpMap" value="true"/>
			<!--通用Mapper接口，多个用逗号隔开-->
			<property name="mappers" value="com.github.abel533.mapper.Mapper,com.github.abel533.hsqldb.HsqldbMapper"/>
		</plugin>
	</plugins>

这里主要是**mappers**参数：

	<property name="mappers" value="com.github.abel533.mapper.Mapper,com.github.abel533.hsqldb.HsqldbMapper"/>  

多个通用Mapper可以用逗号隔开。  

##测试  

接下来编写代码进行测试。  
	
	public interface CountryMapper extends Mapper<Country>,HsqldbMapper<Country> {
	}

在`CountryMapper`上增加继承`HsqldbMapper<Country>`。

编写如下的测试： 
 
	@Test
	public void testDynamicSelectPage() {
	    SqlSession sqlSession = MybatisHelper.getSqlSession();
	    try {
	        CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            //带查询条件的分页查询
	        Country country = new Country();
	        country.setCountrycode("US");
	        List<Country> countryList = mapper.selectPage(country, 0, 10);
	        //查询总数
	        Assert.assertEquals(1, countryList.size());
	        //空参数的查询
	        countryList = mapper.selectPage(new Country(), 100, 10);
	        Assert.assertEquals(10, countryList.size());
	    } finally {
	        sqlSession.close();
	    }
	}

测试输出日志如下：  

	DEBUG [main] - ==>  Preparing: SELECT ID,COUNTRYNAME,COUNTRYCODE FROM COUNTRY WHERE COUNTRYCODE = ? LIMIT ? 
	DEBUG [main] - ==> Parameters: US(String), 10(Integer)
	TRACE [main] - <==    Columns: ID, COUNTRYNAME, COUNTRYCODE
	TRACE [main] - <==        Row: 174, United States of America, US
	DEBUG [main] - <==      Total: 1
	DEBUG [main] - ==>  Preparing: SELECT ID,COUNTRYNAME,COUNTRYCODE FROM COUNTRY LIMIT ? OFFSET ? 
	DEBUG [main] - ==> Parameters: 10(Integer), 100(Integer)
	TRACE [main] - <==    Columns: ID, COUNTRYNAME, COUNTRYCODE
	TRACE [main] - <==        Row: 101, Maldives, MV
	TRACE [main] - <==        Row: 102, Mali, ML
	TRACE [main] - <==        Row: 103, Malta, MT
	TRACE [main] - <==        Row: 104, Mauritius, MU
	TRACE [main] - <==        Row: 105, Mexico, MX
	TRACE [main] - <==        Row: 106, Moldova, Republic of, MD
	TRACE [main] - <==        Row: 107, Monaco, MC
	TRACE [main] - <==        Row: 108, Mongolia, MN
	TRACE [main] - <==        Row: 109, Montserrat Is, MS
	TRACE [main] - <==        Row: 110, Morocco, MA
	DEBUG [main] - <==      Total: 10

测试没有任何问题。  

这里在来点很容易实现的一个功能。上面代码中：

	countryList = mapper.selectPage(new Country(), 100, 10);

传入一个没有设置任何属性的`Country`的时候会查询全部结果。有些人会觉得传入一个空的对象不如传入一个`null`。我们修改测试代码看看结果。  

执行测试代码后抛出异常：

	Caused by: org.apache.ibatis.ognl.OgnlException: source is null for getProperty(null, "id")  

为什么会异常呢，因为我们上面代码中直接引用的`entity.property`，在引用前并没有判断`entity != null`，因而导致了这里的问题。  

我们修改`HsqldbProvider`中的`selectPage`方法，将最后几行代码进行修改，原来的代码：  

	//将if添加到<where>
	sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));

修改后：  

	//增加entity!=null判断
	IfSqlNode ifSqlNode = new IfSqlNode(new MixedSqlNode(ifNodes),"entity!=null");
	//将if添加到<where>
	sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), ifSqlNode));

之后再进行测试就没有问题了。  

##更多例子

更多例子可以参考通用Mapper中的`Mapper<T>`和`MapperProvider`进行参考。代码量不是很大但是实现了常用的这些功能。  

当你了解了原理以及掌握了`SqlNode`的结构后，相信你能写出更多更强大的通用Mapper。  

**我曾经说过会根据不同的数据库写一些针对性的通用Mapper**,当我开始考虑重构的时候，我就想，我应该教会需要这个插件的开发人员如何自己实现。  

一个人的能力是有限的，而且写一个东西开源出来给大家用很容易，但是维护不易。所以呢，我希望觉得这篇文档有用的各位能够分享自己的实现。  

我个人如果有时间，我会考虑增加通用的`Example`查询。`Example`类的设计比较复杂，对应的`SqlNode`结构并不是很复杂。如果有人有兴趣，我可以协助开发`Example`通用查询。  

##结构  

对于刚刚了解上述内容的开发人员来说，`SqlNode`可能没有那么直观，为了便于理解。我在这里将上面最后修改完成的SqlNode以xml的形式写出来。   

	<select id="selectPage" resultType="com.github.abel533.model.Country">
		SELECT ID,COUNTRYNAME,COUNTRYCODE FROM COUNTRY
		<where>
			<if test="entity!=null>
				<if test="entity.id!=null">
					id = #{entity.id}
				</if>
				<if test="entity.countryname!=null">
					countryname = #{entity.countryname}
				</if>
				<if test="entity.countrycode!=null">
					countrycode = #{entity.countrycode}
				</if>
			</if>
		</where>
		<if test="offset==0">
			LIMIT #{limit}
		</if>
		<if test="offset>0">
			LIMIT #{limit} OFFSET #{offset}
		</if>
	</select>

看到这个结构，再和上面代码一一对应应该就不难理解了。熟悉以后，你可能也会觉得JAVA代码方式处理通用的Mapper会容易很多。  


