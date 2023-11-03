package tk.mybatis.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 逻辑删除
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicDelete {

    int isDeletedValue() default 1;

    // 优先级比isDeletedValue更高 表示以null作为删除记录的标识
    boolean isNullForDeletedValue() default false;

    int notDeletedValue() default 0;

    // 优先级比notDeletedValue更高 表示以null作为未删除记录的标识
    boolean isNullForNotDeletedValue() default false;

}
