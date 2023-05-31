package tk.mybatis.mapper.additional.upsert;

import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface UpsertMapper<T> {

    @UpdateProvider(
            type = UpsertProvider.class,
            method = "dynamicSQL"
    )
    void upsert(T record);
}
