package tk.mybatis.mapper.common;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.provider.SaveProvider;

/**
 * 通用Mapper接口,保存
 * <p>判断主键是否存在, 如果存在且不为空执行update语句,如果主键不存在或为空, 执行insert语句</p>
 * Created by YangBin on 2020/5/12
 * Copyright (c) 2020 杨斌 All rights reserved.
 */
public interface SaveMapper<T> {

    /**
     * 保存一个实体，如果实体的主键不为null则更新记录, 主键不存在或主键为null, 则插入记录
     *
     * @param record 不能为空
     * @return
     */
    @InsertProvider(type = SaveProvider.class, method = "dynamicSQL")
    int save(T record);
}
