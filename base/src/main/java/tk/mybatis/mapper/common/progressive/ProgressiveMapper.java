package tk.mybatis.mapper.common.progressive;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Assert;
import tk.mybatis.mapper.util.Sqls;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

/**
 * 渐进式处理Mapper，数据库必须包含id字段
 * @author 陈添明
 */
@RegisterMapper
public interface ProgressiveMapper<T> extends ExampleMapper<T>, SelectByExampleRowBoundsMapper<T> {

    /**
     * 渐进式记录处理
     * 数据表必须包含id字段
     * @param consumer 业务处理逻辑
     * @throws NoSuchFieldException 没有id字段会抛出此异常
     * @throws IllegalAccessException 无法访问id字段会抛出此异常
     */
    default void handleRecordProgressive(Consumer<T> consumer) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clz = getParameterType();
        long i = 0;
        while (true) {
            Example example = Example.builder(clz)
                    .andWhere(Sqls.custom().andGreaterThan("id", i))
                    .build();
            List<T> list = selectByExampleAndRowBounds(example, new RowBounds(0, 100));
            if (list.isEmpty()) {
                return;
            }
            for (T t : list) {
                consumer.accept(t);
            }
            T t = list.get(list.size() - 1);

            Field idField = t.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            i = (Long) idField.get(t);
        }

    }

    /**
     * 渐进式全量删除
     * 数据表必须包含id字段
     * 不会因为数据量大导致慢sql
     * @throws NoSuchFieldException 没有id字段会抛出此异常
     * @throws IllegalAccessException 无法访问id字段会抛出此异常
     */
    default void deleteAllProgressive() throws NoSuchFieldException, IllegalAccessException {
        Class<?> parameterType = getParameterType();
        Long minimumId = getMinimumId();
        if (minimumId == null) {
            return;
        }

        Long maxId = getMaxId();
        if (maxId == null) {
            return;
        }

        for (Long i = minimumId; i <= maxId; ) {
            i = i + 10000;
            Example example = Example.builder(parameterType)
                    .andWhere(Sqls.custom().andBetween("id", minimumId, i))
                    .build();
            deleteByExample(example);
        }
    }

    /**
     * 获取泛型参数Class
     *
     * @return 泛型参数Class
     */
    default Class<?> getParameterType() {
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        Class<?> rawClass = (Class<?>) genericInterfaces[0];
        Type[] rawClassGenericInterfaces = rawClass.getGenericInterfaces();
        Assert.isTrue(rawClassGenericInterfaces.length > 0, "Mapper类必须继承泛型Mapper");
        ParameterizedType mapperType = getParameterizedType(rawClassGenericInterfaces);
        Assert.notNull(mapperType, "未找到参数化Mapper类型！");
        return (Class<?>) mapperType.getActualTypeArguments()[0];
    }

    /**
     * 获取ParameterizedType
     * @param rawClassGenericInterfaces 参数化接口
     * @return ParameterizedType
     */
    default ParameterizedType getParameterizedType(Type[] rawClassGenericInterfaces) {
        for (Type genericInterface : rawClassGenericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                return  (ParameterizedType) genericInterface;
            }
        }
        return null;
    }


    /**
     * 获取最小id
     * @return 最小id
     * @throws NoSuchFieldException 没有id字段会抛出此异常
     * @throws IllegalAccessException 无法访问id字段会抛出此异常
     */
    default Long getMinimumId() throws NoSuchFieldException, IllegalAccessException {
        Class<?> parameterType = getParameterType();
        Example example = Example.builder(parameterType)
                .select("id")
                .orderBy("id")
                .build();

        List<T> list = selectByExampleAndRowBounds(example, new RowBounds(0, 1));
        if (list.isEmpty()) {
            return null;
        }
        T t = list.get(0);
        Field idField = t.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        return (Long) idField.get(t);
    }

    /**
     * 获取最大id
     * @return 最大id
     * @throws NoSuchFieldException 没有id字段会抛出此异常
     * @throws IllegalAccessException 无法访问id字段会抛出此异常
     */
    default Long getMaxId() throws NoSuchFieldException, IllegalAccessException {
        Class<?> parameterType = getParameterType();
        Example example = Example.builder(parameterType)
                .select("id")
                .orderByDesc("id")
                .build();

        List<T> list = selectByExampleAndRowBounds(example, new RowBounds(0, 1));
        if (list.isEmpty()) {
            return null;
        }
        T t = list.get(0);
        Field idField = t.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        return (Long) idField.get(t);
    }


}
