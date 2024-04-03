package tk.mybatis.solon;

import org.apache.ibatis.solon.MybatisAdapter;
import org.apache.ibatis.solon.MybatisAdapterFactory;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;

/**
 * @title: tkMybatis Adapter Factory
 * @author: trifolium.wang
 * @date: 2024/4/1
 * @since 2.7.3
 */
public class TkMapperAdapterFactory implements MybatisAdapterFactory {
    @Override
    public MybatisAdapter create(BeanWrap dsWrap) {
        return new TkMapperMybatisAdapter(dsWrap);
    }

    @Override
    public MybatisAdapter create(BeanWrap dsWrap, Props dsProps) {
        return new TkMapperMybatisAdapter(dsWrap, dsProps);
    }
}
