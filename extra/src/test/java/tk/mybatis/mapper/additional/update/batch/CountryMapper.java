package tk.mybatis.mapper.additional.update.batch;

import tk.mybatis.mapper.additional.Country;
import tk.mybatis.mapper.common.BaseMapper;

public interface CountryMapper extends BaseMapper<Country>, BatchUpdatePropertyByPrimaryKeyMapper<Country, Long> {
}
