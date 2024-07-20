package tk.mybatis.mapper.common.example;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.List;


/**
 * 通过相关的 {@link Example} 查询对象，查询结果集中按照指定列进行分区排序并且指定排名的所有结果对象，
 * 这个查询相当于 SQL 中的开窗函数 ROW_NUMBER 查询
 *
 * @author xhliu
 * @param <T> 相关的实体类数据类型
 */
@RegisterMapper
public interface SelectRowNumberByExampleMapper<T> {

    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<T> selectRowNumberByExample(Example example);
}
