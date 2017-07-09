# Mapper3通用接口大全

Mapper3接口有两种形式，一种是提供了一个方法的接口。还有一种是不提供方法，但是继承了多个单方法的接口，一般是某类方法的集合。

例如`SelectMapper<T>`是一个单方法的接口，`BaseSelectMapper<T>`是一个继承了4个基础查询方法的接口。

## 基础接口

### Select

接口：`SelectMapper<T>`<br>
方法：`List<T> select(T record);`<br>
说明：根据实体中的属性值进行查询，查询条件使用等号<br><br>

接口：`SelectByPrimaryKeyMapper<T>`<br>
方法：`T selectByPrimaryKey(Object key);`<br>
说明：根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号<br>
<br>

接口：`SelectAllMapper<T>`<br>
方法：`List<T> selectAll();`<br>
说明：查询全部结果，select(null)方法能达到同样的效果<br>
<br>

接口：`SelectOneMapper<T>`<br>
方法：`T selectOne(T record);`<br>
说明：根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号<br><br>

接口：`SelectCountMapper<T>`<br>
方法：`int selectCount(T record);`<br>
说明：根据实体中的属性查询总数，查询条件使用等号<br><br>

### Insert

接口：`InsertMapper<T>`<br>
方法：`int insert(T record);`<br>
说明：保存一个实体，null的属性也会保存，不会使用数据库默认值<br><br>

接口：`InsertSelectiveMapper<T>`<br>
方法：`int insertSelective(T record);`<br>
说明：保存一个实体，null的属性不会保存，会使用数据库默认值<br><br>

### Update

接口：`UpdateByPrimaryKeyMapper<T>`<br>
方法：`int updateByPrimaryKey(T record);`<br>
说明：根据主键更新实体全部字段，null值会被更新<br><br>

接口：`UpdateByPrimaryKeySelectiveMapper<T>`<br>
方法：`int updateByPrimaryKeySelective(T record);`<br>
说明：根据主键更新属性不为null的值<br><br>

### Delete

接口：`DeleteMapper<T>`<br>
方法：`int delete(T record);`<br>
说明：根据实体属性作为条件进行删除，查询条件使用等号<br><br>

接口：`DeleteByPrimaryKeyMapper<T>`<br>
方法：`int deleteByPrimaryKey(Object key);`<br>
说明：根据主键字段进行删除，方法参数必须包含完整的主键属性<br><br>

### base组合接口

接口：`BaseSelectMapper<T>`<br>
方法：包含上面Select的4个方法<br><br>

接口：`BaseInsertMapper<T>`<br>
方法：包含上面Insert的2个方法<br><br>

接口：`BaseUpdateMapper<T>`<br>
方法：包含上面Update的2个方法<br><br>

接口：`BaseDeleteMapper<T>`<br>
方法：包含上面Delete的2个方法<br><br>

### CRUD组合接口

接口：`BaseMapper<T>`<br>
方法：继承了base组合接口中的4个组合接口，包含完整的CRUD方法<br><br>

## Example方法

接口：`SelectByExampleMapper<T>`<br>
方法：`List<T> selectByExample(Object example);`<br>
说明：根据Example条件进行查询<br>
<b>重点：</b>这个查询支持通过`Example`类指定查询列，通过`selectProperties`方法指定查询列<br><br>


接口：`SelectCountByExampleMapper<T>`<br>
方法：`int selectCountByExample(Object example);`<br>
说明：根据Example条件进行查询总数<br><br>

接口：`UpdateByExampleMapper<T>`<br>
方法：`int updateByExample(@Param("record") T record, @Param("example") Object example);`<br>
说明：根据Example条件更新实体`record`包含的全部属性，null值会被更新<br><br>

接口：`UpdateByExampleSelectiveMapper<T>`<br>
方法：`int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);`<br>
说明：根据Example条件更新实体`record`包含的不是null的属性值<br><br>

接口：`DeleteByExampleMapper<T>`<br>
方法：`int deleteByExample(Object example);`<br>
说明：根据Example条件删除数据<br><br>

### Example组合接口

接口：`ExampleMapper<T>`<br>
方法：包含上面Example中的5个方法<br><br>

## Condition方法

Condition方法和Example方法作用完全一样，只是为了避免Example带来的歧义，提供的的Condition方法

接口：`SelectByConditionMapper<T>`<br>
方法：`List<T> selectByCondition(Object condition);`<br>
说明：根据Condition条件进行查询<br><br>

接口：`SelectCountByConditionMapper<T>`<br>
方法：`int selectCountByCondition(Object condition);`<br>
说明：根据Condition条件进行查询总数<br><br>

接口：`UpdateByConditionMapper<T>`<br>
方法：`int updateByCondition(@Param("record") T record, @Param("example") Object condition);`<br>
说明：根据Condition条件更新实体`record`包含的全部属性，null值会被更新<br><br>

接口：`UpdateByConditionSelectiveMapper<T>`<br>
方法：`int updateByConditionSelective(@Param("record") T record, @Param("example") Object condition);`<br>
说明：根据Condition条件更新实体`record`包含的不是null的属性值<br><br>

接口：`DeleteByConditionMapper<T>`<br>
方法：`int deleteByCondition(Object condition);`<br>
说明：根据Condition条件删除数据<br><br>

### Condition组合接口

接口：`ConditionMapper<T>`<br>
方法：包含上面Condition中的5个方法<br><br>

## RowBounds

默认为<b>内存分页</b>，可以配合[PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)实现物理分页

接口：`SelectRowBoundsMapper<T>`<br>
方法：`List<T> selectByRowBounds(T record, RowBounds rowBounds);`<br>
说明：根据实体属性和RowBounds进行分页查询<br><br>

接口：`SelectByExampleRowBoundsMapper<T>`<br>
方法：`List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds);`<br>
说明：根据example条件和RowBounds进行分页查询<br><br>

接口：`SelectByConditionRowBoundsMapper<T>`<br>
方法：`List<T> selectByConditionAndRowBounds(Object condition, RowBounds rowBounds);`<br>
说明：根据example条件和RowBounds进行分页查询，该方法和selectByExampleAndRowBounds完全一样，只是名字改成了Condition<br><br>

### RowBounds组合接口

接口：`RowBoundsMapper<T>`<br>
方法：包含上面RowBounds中的前两个方法，不包含`selectByConditionAndRowBounds`<br><br>

## special特殊接口

这些接口针对部分数据库设计，不是所有数据库都支持

接口：`InsertListMapper<T>`<br>
方法：`int insertList(List<T> recordList);`<br>
说明：批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列<br><br>

接口：`InsertUseGeneratedKeysMapper<T>`<br>
方法：`int insertUseGeneratedKeys(T record);`<br>
说明：插入数据，限制为实体包含`id`属性并且必须为自增列，实体配置的主键策略无效<br><br>

## MySQL专用

接口：`MySqlMapper<T>`<br>
继承方法：`int insertList(List<T> recordList);`<br>
继承方法：`int insertUseGeneratedKeys(T record);`<br>
说明：该接口不包含方法，继承了special中的`InsertListMapper<T>`和`InsertUseGeneratedKeysMapper<T>`<br><br>

## SqlServer专用

由于sqlserver中插入自增主键时，不能使用`null`插入，不能在insert语句中出现`id`。

注意SqlServer的两个特有插入方法都使用了

`@Options(useGeneratedKeys = true, keyProperty = "id")`

这就要求表的主键为`id`，且为自增，如果主键不叫`id`可以看高级教程中的解决方法。

另外这俩方法和<b>base</b>中的插入方法重名，不能同时存在！

如果某种数据库和SqlServer这里类似，也可以使用这些接口（需要先测试）。

接口：`InsertMapper`<br>
方法：`int insert(T record);`<br>
说明：插入数据库，`null`值也会插入，不会使用列的默认值<br><br>

接口：`InsertSelectiveMapper`<br>
方法：`int insertSelective(T record);`<br>
说明：插入数据库，null的属性不会保存，会使用数据库默认值<br><br>

接口：`SqlServerMapper`<br>
说明：这是上面两个接口的组合接口。<br><br>

## Ids接口

通过操作ids字符串进行操作，ids 如 "1,2,3" 这种形式的字符串，这个方法要求实体类中有且只有一个带有`@Id`注解的字段，否则会抛出异常。

接口：`SelectByIdsMapper`<br>
方法：`List<T> selectByIds(String ids)`<br>
说明：根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段<br><br>

接口：`DeleteByIdsMapper`<br>
方法：`int deleteByIds(String ids)`<br>
说明：根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段<br><br>

### Ids组合接口

接口：`IdsMapper<T>`<br>
方法：包含上面Ids中的前两个方法<br><br>

## Mapper<T>接口

接口：`Mapper<T>`<br>
该接口兼容Mapper2.x版本，继承了`BaseMapper<T>`, `ExampleMapper<T>`, `RowBoundsMapper<T>`三个组合接口。<br><br>

