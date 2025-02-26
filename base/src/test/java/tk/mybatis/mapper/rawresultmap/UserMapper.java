package tk.mybatis.mapper.rawresultmap;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT * FROM user")
    List<User> selectRawAnnotation();

    @Select("SELECT * FROM user")
    @ResultMap("BaseResultMap")
    List<User> selectRawAnnotationResultMap();

    List<User> fetchRawResultType();

    List<User> fetchDynamicSqlType(@Param("ids") List<Integer> ids);

    List<User> fetchRawResultMap();

    List<User> fetchDynamicResultMap();

    Map<String, Object> getMapUser();

    Integer selectCount2();
}
