package tk.mybatis.mapper.additional.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.weekend.Fn;

/**
 * @param <T> 不能为空
 * @author jingkaihui
 * @date 2020/3/30
 */
@RegisterMapper
public interface DeleteByPropertyMapper<T> {

    /**
     * 根据实体中的属性删除，条件使用等号
     *
     * @param fn    属性
     * @param value 属性值
     * @return
     */
    @DeleteProvider(type = DeletePropertyProvider.class, method = "dynamicSQL")
    int deleteByProperty(@Param("fn") Fn<T, ?> fn, @Param("value") Object value);

    /**
     * 根据实体中的属性删除，条件使用 in
     *
     * @param fn    属性
     * @param value 属性值
     * @return
     */
    @DeleteProvider(type = DeletePropertyProvider.class, method = "dynamicSQL")
    int deleteInByProperty(@Param("fn") Fn<T, ?> fn, @Param("values") Object value);

    /**
     * 根据属性及对应值进行删除，删除条件使用 between
     *
     * @param fn    属性
     * @param begin 开始值
     * @param end   开始值
     * @return
     */
    @SelectProvider(type = DeletePropertyProvider.class, method = "dynamicSQL")
    int deleteBetweenByProperty(@Param("fn") Fn<T, ?> fn, @Param("begin") Object begin, @Param("end") Object end);
}
