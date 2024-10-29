package tk.mybatis.mapper.additional.select;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.weekend.Fn;

import java.util.List;

/**
 * 查询指定字段
 *
 * @param <T> 不能为空
 *
 * @author xiaodiwangjie
 * @date 2020/10/24
 */
@RegisterMapper
public interface SelectSpecifyColumnsMapper<T> {

    /**
     * 根据条件查询实体, 并指定字段
     *
     * @param record  查询实体条件
     * @param columns 指定的返回字段
     * @return
     */
    @SelectProvider(
            type = SelectSpecifyColumnsProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectByColumns(@Param("record") T record, @Param("columns") List<String> columns);

    /**
     * 根据条件查询实体, 并指定属性
     *
     * @param record 查询实体条件
     * @param fns    指定的返回实体属性
     * @return
     */
    @SelectProvider(
            type = SelectSpecifyColumnsProvider.class,
            method = "dynamicSQL"
    )
    List<T> selectByPropertys(@Param("record") T record, @Param("fns") List<Fn<T, ?>> fns);
}
