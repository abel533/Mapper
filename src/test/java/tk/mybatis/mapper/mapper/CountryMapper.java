package tk.mybatis.mapper.mapper;

import tk.mybatis.mapper.hsqldb.HsqldbMapper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by liuzh on 2014/11/19.
 */
public interface CountryMapper extends Mapper<Country>, HsqldbMapper<Country>, MySqlMapper<Country> {
}
