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

package tk.mybatis.mapper.additional.dialect.oracle;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import tk.mybatis.mapper.additional.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Oracle测试类
 */
@Ignore("需要Oracle数据源")
public class OracleTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    };

    @Override
    protected void runSql(Reader reader) {
    }

    @Test
    public void testSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            DemoCountryMapper mapper = sqlSession.getMapper(DemoCountryMapper.class);
            List<DemoCountry> countries = mapper.selectAll();
            System.out.println(countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsertList() {
        SqlSession sqlSession = getSqlSession();
        try {
            DemoCountryMapper mapper = sqlSession.getMapper(DemoCountryMapper.class);
            List<DemoCountry> countryList = new ArrayList<DemoCountry>();
            countryList.add(new DemoCountry("20", "Zimbabwe","ZW"));
            countryList.add(new DemoCountry("21", "Zaire","ZR"));
            countryList.add(new DemoCountry("22", "Zambia","ZM"));
            int updates = mapper.insertList(countryList);
            Assert.assertEquals(3, updates);
        } finally {
            //sqlSession.commit();
            sqlSession.close();
        }
    }
}
