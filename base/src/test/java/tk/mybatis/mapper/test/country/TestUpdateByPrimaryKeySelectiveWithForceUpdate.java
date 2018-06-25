/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.test.country;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.mapper.CountryIntMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.CountryInt;

import java.util.Arrays;

/**
 * @Description:  验证数值空值强制更新
 * @author qrqhuang
 * @date 2018-06-25
 */
public class TestUpdateByPrimaryKeySelectiveWithForceUpdate {

    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveWithForceUpdateByEmpty() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIntMapper mapper = sqlSession.getMapper(CountryIntMapper.class);
            CountryInt country = new CountryInt();
            country.setId(174);
            country.setCountryname("英国");
            mapper.updateByPrimaryKeySelectiveWithForceUpdate(country, null);

            country = mapper.selectByPrimaryKey(174);
            Assert.assertNotNull(country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveWithForceUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryIntMapper mapper = sqlSession.getMapper(CountryIntMapper.class);
            CountryInt country = new CountryInt();
            country.setId(174);
            country.setCountryname("英国");
            mapper.updateByPrimaryKeySelectiveWithForceUpdate(country, Arrays.asList("countrycode", "countryname"));

            country = mapper.selectByPrimaryKey(174);
            Assert.assertNull(country.getCountrycode());
        } finally {
            sqlSession.close();
        }
    }
  }
