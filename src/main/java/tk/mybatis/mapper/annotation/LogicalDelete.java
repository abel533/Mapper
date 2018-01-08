package tk.mybatis.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 逻辑删除实体类标记，用于实现实体逻辑删除标记.
 *
 * @see tk.mybatis.mapper.provider.base.BaseDeleteProvider
 * @author CarlJia.
 * @date 2018-01-08.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicalDelete {
}
