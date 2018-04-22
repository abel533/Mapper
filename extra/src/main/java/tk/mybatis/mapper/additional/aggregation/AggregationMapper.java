package tk.mybatis.mapper.additional.aggregation;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 通用聚合查询接口,特殊方法
 *
 * @author liuchan
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface AggregationMapper<T> {

    /**
     * 根据example和aggregateCondition进行聚合查询
     * 分组不支持having条件过滤， 如需要建议使用xml文件
     *
     * @param example
     * @param aggregateCondition 可以设置聚合查询的属性和分组属性
     * @return 返回聚合查询属性和分组属性的值
     */
    @SelectProvider(type = AggregationProvider.class, method = "dynamicSQL")
    List<T> selectAggregationByExample(@Param("example") Object example, @Param("aggregateCondition") AggregateCondition aggregateCondition);

}
