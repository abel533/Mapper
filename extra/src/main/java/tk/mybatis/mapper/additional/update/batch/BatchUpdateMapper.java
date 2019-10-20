package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface BatchUpdateMapper<T> {

    @UpdateProvider(
            type = BatchUpdateProvider.class,
            method = "dynamicSQL"
    )
    void batchUpdate(@Param("list") List<? extends T> recordList);
}
