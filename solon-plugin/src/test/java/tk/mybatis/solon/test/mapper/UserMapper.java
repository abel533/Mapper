package tk.mybatis.solon.test.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.solon.test.entity.User;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @ResultMap("tk.mybatis.solon.test.mapper.UserMapper.BaseResultMap")
    @Select("SELECT * FROM user WHERE is_del = 0 AND age > #{age}")
    List<User> findByGTAge(@Param("age") Integer age);

    List<User> findByName(@Param("name") String name);
}