package tk.mybatis.mapper.entity;

/**
 * Condition - 条件查询，命名就是任性
 *
 * @author liuzh
 * @since 2015-06-10
 */
public class Condition extends Example {
    public Condition(Class<?> entityClass) {
        super(entityClass);
    }

    public Condition(Class<?> entityClass, boolean exists) {
        super(entityClass, exists);
    }
}
