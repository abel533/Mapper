package tk.mybatis.mapper.hsqldb;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 扩展例子
 *
 * @author liuzh
 */
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
