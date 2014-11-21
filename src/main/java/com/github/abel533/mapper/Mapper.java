package com.github.abel533.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by liuzh on 2014/11/19.
 */
public interface Mapper<T> {

    @SelectProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    List<T> select(T record);

    @SelectProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int selectCount(T record);

    @SelectProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    T selectByPrimaryKey(Object key);

    @InsertProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int insert(T record);

    @InsertProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int insertSelective(T record);

    @DeleteProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int delete(T key);

    @DeleteProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int deleteByPrimaryKey(Object key);

    @UpdateProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int updateByPrimaryKey(T record);

    @UpdateProvider(type = MapperHelper.class, method = MapperHelper.DYNAMIC_SQL)
    int updateByPrimaryKeySelective(T record);

}
