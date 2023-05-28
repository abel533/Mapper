package tk.mybatis.mapper.code;

/**
 * 执行 SQL 的时机
 *
 * @author liuzh
 */
public enum ORDER {

    //insert 后执行 SQL
    AFTER,
    //insert 前执行 SQL
    BEFORE,
    //使用全局配置
    DEFAULT
}
