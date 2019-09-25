package tk.mybatis.mapper.common;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.pagable.SelectPageByConditionMapper;
import tk.mybatis.mapper.common.pagable.SelectPageByExampleMapper;
import tk.mybatis.mapper.common.pagable.SelectPageMapper;

@RegisterMapper
public interface PagableMapper<T> extends
        SelectPageMapper<T>,
        SelectPageByExampleMapper<T>,
        SelectPageByConditionMapper<T>{
}
