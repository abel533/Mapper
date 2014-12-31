package test.mapper;

import com.github.abel533.hsqldb.HsqldbMapper;
import com.github.abel533.mapper.Mapper;
import test.model.UserLoginInfo;

public interface UserLoginInfoMapper extends HsqldbMapper<UserLoginInfo>, Mapper<UserLoginInfo> {
}