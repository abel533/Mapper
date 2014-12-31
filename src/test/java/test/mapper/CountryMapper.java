package test.mapper;

import com.github.abel533.hsqldb.HsqldbMapper;
import com.github.abel533.mapper.Mapper;
import test.model.Country;

public interface CountryMapper extends HsqldbMapper<Country>, Mapper<Country> {
}