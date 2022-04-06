package tk.mybatis.mapper.additional.select;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.weekend.Fn;

import java.util.List;

/**
 * 根据属性查询接口
 *
 * @param <T> 不能为空
 * @author jingkaihui
 * @date 2019/10/11
 */
@RegisterMapper
public interface SelectByPropertyMapper<T> {

    /**
     * 根据属性及对应值进行查询，只能有一个返回值，有多个结果时抛出异常，查询条件使用等号
     *
     * @param fn    查询属性
     * @param value 属性值
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    T selectOneByProperty(@Param("fn") Fn<T, ?> fn, @Param("value") Object value);

    /**
     * 根据属性及对应值进行查询，有多个返回值，查询条件使用等号
     *
     * @param fn    查询属性
     * @param value 属性值
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    List<T> selectByProperty(@Param("fn") Fn<T, ?> fn, @Param("value") Object value);

    /**
     * 根据属性及对应值进行查询，查询条件使用 in
     *
     * @param fn     查询属性
     * @param values 属性值集合，集合不能空
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    List<T> selectInByProperty(@Param("fn") Fn<T, ?> fn, @Param("values") List<?> values);

    /**
     * 根据属性及对应值进行查询，查询条件使用 between
     *
     * @param fn    查询属性
     * @param begin 开始值
     * @param end   开始值
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    List<T> selectBetweenByProperty(@Param("fn") Fn<T, ?> fn, @Param("begin") Object begin, @Param("end") Object end);

    /**
     * 根据属性及对应值进行查询，检查是否存在对应记录，查询条件使用等号
     *
     * @param fn    查询属性
     * @param value 属性值
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    boolean existsWithProperty(@Param("fn") Fn<T, ?> fn, @Param("value") Object value);

    /**
     * 根据属性及对应值进行查询，统计符合条件的记录数，查询条件使用等号
     *
     * @param fn    查询属性
     * @param value 属性值
     * @return
     */
    @SelectProvider(type = SelectPropertyProvider.class, method = "dynamicSQL")
    int selectCountByProperty(@Param("fn") Fn<T, ?> fn, @Param("value") Object value);
}
