package tk.mybatis.mapper.entity;

/**
 *
 * @author jinyanan
 * @version 3.6
 */
public interface Predicate<T> {

    /**
     * 返回处理input后的结果
     * @param input 传入参数
     * @return 根据input处理后的结果
     */
    boolean apply(T input);

}