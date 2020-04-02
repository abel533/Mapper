package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.weekend.Fn;

import java.util.List;

@RegisterMapper
public interface BatchUpdatePropertyByPrimaryKeyMapper<T, PK> {

    /**
     * 多条数据更新同一字段为相同值
     * @param fn           字段名
     * @param value        更新的字段值
     * @param primaryKeys  数据主键集合
     * @return
     */
    @UpdateProvider(type = BatchUpdatePropertyByPrimaryKeyProvider.class, method = "dynamicSQL")
    int batchUpdateFieldByIdList(@Param("fn") Fn<T, ?> fn, @Param("value") Object value, @Param("idList") List<PK> primaryKeys);

    /**
     * 多条数据更新多个字段为相同值
     * @param fieldValues 更新字段及更新值
     * @param primaryKeys 数据主键集合
     * @return
     */
    @UpdateProvider(type = BatchUpdatePropertyByPrimaryKeyProvider.class, method = "dynamicSQL")
    int batchUpdateFieldListByIdList(@Param("fieldList") List<FieldValue<T>> fieldValues, @Param("idList") List<PK> primaryKeys);
}
