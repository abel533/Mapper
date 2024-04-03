package tk.mybatis.solon;

import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @title: TkMybatis的Solon插件
 * @author: trifolium.wang
 * @date: 2024/4/1
 * @since 2.7.3
 */
public class XPluginImpl implements Plugin {


    @Override
    public void start(AppContext context) throws Throwable {

        MybatisAdapterManager.setAdapterFactory(new TkMapperAdapterFactory());
    }

}
