package com.github.abel533.mapper;

import com.github.abel533.hsqldb.HsqldbMapper;
import com.github.abel533.model.Country;

/**
 * Created by liuzh on 2014/11/19.
 */
public interface CountryMapper extends Mapper<Country>,HsqldbMapper<Country> {
}
