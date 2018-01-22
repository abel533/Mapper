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

package tk.mybatis.mapper.test.weekend;

/**
 * 测试WeekendSql构建者模式类 由于方法引用需要jdk8
 * 执行该测试的时候需要临时将pom.xml中maven-compiler-plugin插件jdk编译等级调整为1.8
 * 为了防止jdk6编译等级打包出错，将测试用例全部注释
 * <p>
 * <pre>
 * 		<plugin>
 * <artifactId>maven-compiler-plugin</artifactId>
 * <configuration>
 * <source>1.8</source>
 * <target>1.8</target>
 * </configuration>
 * </plugin>
 * </pre>
 *
 * @author XuYin
 */
public class WeekendSqlsTest {
    /*
    @Test
    public void testWeekend() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> selectByExample = mapper.selectByExample(
                    new Example.Builder(Country.class).where(Sqls.custom().andLike("countryname", "China")).build());
            List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "China")).build());
            // 判断两个结果数组内容是否相同
            Assert.assertArrayEquals(selectByExample.toArray(), selectByWeekendSql.toArray());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testWeekendComplex() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> selectByExample = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(Sqls.custom().andLike("countryname", "%a%").andGreaterThan("countrycode", "123")).build());
            List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
                    .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "%a%")
                            .andGreaterThan(Country::getCountrycode, "123"))
                    .build());
            // 判断两个结果数组内容是否相同
            Assert.assertArrayEquals(selectByExample.toArray(), selectByWeekendSql.toArray());
        } finally {
            sqlSession.close();
        }
    }
    */
}
