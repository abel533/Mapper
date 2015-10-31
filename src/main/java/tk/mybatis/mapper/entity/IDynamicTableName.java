package tk.mybatis.mapper.entity;

/**
 * 实现动态表名时，实体类需要实现该接口
 *
 * @author liuzh
 * @since 2015-10-28 22:20
 */
public interface IDynamicTableName {

    /**
     * 获取动态表名 - 只要有返回值，不是null和''，就会用返回值作为表名
     *
     * @return
     */
    String getDynamicTableName();
}
