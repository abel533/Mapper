package tk.mybatis.mapper.common.ids;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.IdsProvider;

import java.util.List;

/**
 * 通用Mapper接口,根据ids查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
public interface SelectByIdsMapper<T> {

    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ids 如 "1,2,3,4"
     * @return
     */
    @SelectProvider(type = IdsProvider.class, method = "dynamicSQL")
    List<T> selectByIds(String ids);

}
