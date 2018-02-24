package tk.mybatis.mapper.cache;

import org.apache.ibatis.annotations.CacheNamespace;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.common.Mapper;

/**
 * 只有接口时，加下面的注解即可
 */
@CacheNamespace
public interface CountryCacheMapper extends Mapper<Country> {

}
