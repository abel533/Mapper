package tk.mybatis.mapper.additional.update.differ;


import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.Country;

public interface CountryMapper extends UpdateByDifferMapper<Country> {

    @Select("select * from country where id = #{id}")
    Country selectByPrimaryKey(Long id);
}
