package tk.mybatis.mapper.entity;

/**
 * 实现动态表名时，实体类实现该接口
 *
 * @author liuzh
 * @since 2015-10-28 22:20
 */
public interface IDynamicTableName {

    /**
     * 获取动态表名 - 这个方法是关键，只要有返回值，不是null和''，就会用返回值作为表名
     *
     * @return
     */
    String getDynamicTableName();

    /**
     * 设置动态表名 - 这个方法没有绝对的作用，仅仅是为了和上面的get方法配套
     *
     * @param dynamicTableName
     */
    void setDynamicTableName(String dynamicTableName);
}
