package tk.mybatis.mapper.common.special;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.provider.SpecialProvider;

/**
 * 通用Mapper接口,特殊方法，批量插入，支持批量插入的数据库都可以使用，例如mysql,h2等
 * @author 陈添明
 * @date 2019/9/16
 */
@RegisterMapper
public interface InsertListSelectiveMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     * 如果插入实体的属性值为null，则使用数据库的默认值
     * 避免数据列设置了非空约束时，插入数据失败！
     *
     * @param iterable
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = SpecialProvider.class, method = "dynamicSQL")
    void insertListSelective(Iterable<T> iterable);
}
