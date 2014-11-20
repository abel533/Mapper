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

    @SelectProvider(type = MapperHelper.class, method = "dynamicSQL")
    List<T> select(T record);

    @SelectProvider(type = MapperHelper.class, method = "dynamicSQL")
    T selectByPrimaryKey(Object key);

    @InsertProvider(type = MapperHelper.class, method = "dynamicSQL")
    int insert(T record);

    @InsertProvider(type = MapperHelper.class, method = "dynamicSQL")
    int insertSelective(T record);

    @DeleteProvider(type = MapperHelper.class, method = "dynamicSQL")
    int deleteByPrimaryKey(Object key);

    @UpdateProvider(type = MapperHelper.class, method = "dynamicSQL")
    int updateByPrimaryKey(T record);

    @UpdateProvider(type = MapperHelper.class, method = "dynamicSQL")
    int updateByPrimaryKeySelective(T record);

}
