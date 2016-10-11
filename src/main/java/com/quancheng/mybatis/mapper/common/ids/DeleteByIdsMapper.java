package com.quancheng.mybatis.mapper.common.ids;

import org.apache.ibatis.annotations.DeleteProvider;
import com.quancheng.mybatis.mapper.provider.IdsProvider;

/**
 * 通用Mapper接口,根据ids删除
 *
 * @param <T> 不能为空
 * @author liuzh
 */
public interface DeleteByIdsMapper<T> {

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return
     */
    @DeleteProvider(type = IdsProvider.class, method = "dynamicSQL")
    int deleteByIds(String ids);

}
