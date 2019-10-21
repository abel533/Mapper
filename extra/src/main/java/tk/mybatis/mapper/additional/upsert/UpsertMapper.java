package tk.mybatis.mapper.additional.upsert;

import org.apache.ibatis.annotations.UpdateProvider;

public interface UpsertMapper<T> {

    @UpdateProvider(
            type = UpsertProvider.class,
            method = "dynamicSQL"
    )
    void upsert(T record);
}
