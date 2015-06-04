#提供的可选的接口

##基础接口

###select

接口：`SelectMapper`
方法：`List<T> select(T record);`
说明：根据实体中的属性值进行查询，查询条件使用等号

接口：`SelectByPrimaryKeyMapper`
方法：`T selectByPrimaryKey(Object key);`
说明：根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号

接口：`SelectOneMapper`
方法：`T selectOne(T record);`
说明：根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号

接口：`SelectCountMapper`
方法：`int selectCount(T record);`
说明：根据实体中的属性查询总数，查询条件使用等号

###insert

接口：``
方法：``
说明：

接口：``
方法：``
说明：

###update

接口：``
方法：``
说明：

接口：``
方法：``
说明：

###delete

接口：``
方法：``
说明：

接口：``
方法：``
说明：

