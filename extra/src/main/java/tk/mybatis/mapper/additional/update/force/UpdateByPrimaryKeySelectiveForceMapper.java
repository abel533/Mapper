package tk.mybatis.mapper.additional.update.force;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * @Description:  通用Mapper接口,更新,强制
 * @author qrqhuangcy
 * @date 2018-06-26
 */
@RegisterMapper
public interface UpdateByPrimaryKeySelectiveForceMapper<T> {

    /**
     * 根据主键更新属性不为null的值, 指定的属性(null值)会被强制更新
     * @param record
     * @param forceUpdateProperties
     * @return
     */
    @UpdateProvider(type = UpdateByPrimaryKeySelectiveForceProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelectiveForce(@Param("record") T record, @Param("forceUpdateProperties") List<String> forceUpdateProperties);
}
