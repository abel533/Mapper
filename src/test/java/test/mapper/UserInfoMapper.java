package test.mapper;

import com.github.abel533.hsqldb.HsqldbMapper;
import com.github.abel533.mapper.Mapper;
import test.model.UserInfo;

public interface UserInfoMapper extends HsqldbMapper<UserInfo>, Mapper<UserInfo> {
}