package tk.mybatis.mapper.common.base.update;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.base.BaseUpdateProvider;

import java.util.List;

/**
 * @Description:  通用Mapper接口,更新,强制
 * @author qrqhu
 * @date 2018-06-26
 */
public interface UpdateByPrimaryKeySelectiveWithForceUpdateMapper<T> {

    /**
     * 根据主键更新属性不为null的值, 指定的属性(null值)会被强制更新
     * @param record
     * @param forceUpdateProperties
     * @return
     */
    @UpdateProvider(type = BaseUpdateProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelectiveWithForceUpdate(@Param("record") T record, @Param("forceUpdateProperties") List<String> forceUpdateProperties);
}
