package tk.mybatis.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 字段排序
 * @author: qrqhuangcy
 * @date: 2018-11-11
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    /**
     * 升降序
     * @return
     */
    String value() default "ASC";

    /**
     * 优先级, 值小的优先
     * @return
     */
    int priority() default 1;
}
