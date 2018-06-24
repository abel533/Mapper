package tk.mybatis.mapper.additional.update.differ;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 差异更新
 *
 * @param <T> 不能为空
 * @author liuzh
 * @since 4.0.4
 */
@RegisterMapper
public interface UpdateByDifferMapper<T> {

    /**
     * 根据 old 和 newer 进行差异更新，当对应某个字段值不同时才会更新
     *
     * @param old
     * @param newer
     * @return
     */
    @UpdateProvider(type = UpdateByDifferProvider.class, method = "dynamicSQL")
    int updateByDiffer(@Param("old") T old, @Param("newer") T newer);
}
