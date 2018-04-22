package tk.mybatis.mapper.genid;

/**
 * 不提供具体的实现，这里提供一个思路。<br/>
 *
 * 在 Spring 集成环境中，可以通过配置静态方式获取 Spring 的 context 对象。<br/>
 *
 * 如果使用 vesta(https://gitee.com/robertleepeak/vesta-id-generator) 来生成 ID，假设已经提供了 vesta 的 idService。<br/>
 *
 * 那么可以在实现中获取该类，然后生成 Id 返回，示例代码如下：
 *
 * <pre>
 * public class VestaGenId implement GenId<Long> {
 *    public Long genId(String table, String column){
 *        //ApplicationUtil.getBean 需要自己实现
 *        IdService idService = ApplicationUtil.getBean(IdService.class);
 *        return idService.genId();
 *    }
 * }
 * </pre>
 *
 * @author liuzh
 */
public interface GenId<T> {
    class NULL implements GenId {
        @Override
        public Object genId(String table, String column) {
            throw new UnsupportedOperationException();
        }
    }

    T genId(String table, String column);

}
