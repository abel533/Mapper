/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 the original author or authors.
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
 *
 */

package tk.mybatis.mapper.weekend;

/**
 * {@link WeekendSqls} 的工具类，提供一系列静态方法，减少泛型参数的指定，使代码更简洁、清晰
 *
 * 直接使用WeekSqls，以下的查询需要指定两次Country类：
 *  List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
 *         .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "%a%")
 *                 .andGreaterThan(Country::getCountrycode, "123"))
 *         .build());
 *
 * 使用 WeekendSqlsUtils，只需指定一次Country类：
 *  List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
 *                     .where(WeekendSqlsUtils.andLike(Country::getCountryname, "%a%")
 *                             .andGreaterThan(Country::getCountrycode, "123"))
 *                     .build());
 *
 *  建议使用 import static，代码会简洁一些
 *  import static tk.mybatis.mapper.weekend.WeekendSqlsUtils.andLike;
 *
 *  List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
 *                     .where(andLike(Country::getCountryname, "%a%")
 *                             .andGreaterThan(Country::getCountrycode, "123"))
 *                     .build());
 * @author linweichao
 * @date 2019/5/20
 */
public class WeekendSqlsUtils {

    public static <T> WeekendSqls<T> andIsNull(String property) {
        return WeekendSqls.<T>custom().andIsNull(property);
    }

    public static <T> WeekendSqls<T> andIsNull(Fn<T, Object> fn) {
        return WeekendSqls.<T>custom().andIsNull(fn);
    }

    public static <T> WeekendSqls<T> andIsNotNull(String property) {
        return WeekendSqls.<T>custom().andIsNotNull(property);
    }

    public static <T> WeekendSqls<T> andIsNotNull(Fn<T, Object> fn) {
        return WeekendSqls.<T>custom().andIsNotNull(fn);
    }

    public static <T> WeekendSqls<T> andEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().andEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> andEqualTo(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> andNotEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().andNotEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> andNotEqualTo(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andNotEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> andGreaterThan(String property, Object value) {
        return WeekendSqls.<T>custom().andGreaterThan(property, value);
    }

    public static <T> WeekendSqls<T> andGreaterThan(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andGreaterThan(fn, value);
    }

    public static <T> WeekendSqls<T> andGreaterThanOrEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().andGreaterThanOrEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> andGreaterThanOrEqualTo(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andGreaterThanOrEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> andLessThan(String property, Object value) {
        return WeekendSqls.<T>custom().andLessThan(property, value);
    }

    public static <T> WeekendSqls<T> andLessThan(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andLessThan(fn, value);
    }

    public static <T> WeekendSqls<T> andLessThanOrEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().andLessThanOrEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> andLessThanOrEqualTo(Fn<T, Object> fn, Object value) {
        return WeekendSqls.<T>custom().andLessThanOrEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> andIn(String property, Iterable values) {
        return WeekendSqls.<T>custom().andIn(property, values);
    }

    public static <T> WeekendSqls<T> andIn(Fn<T, Object> fn, Iterable values) {
        return WeekendSqls.<T>custom().andIn(fn, values);
    }

    public static <T> WeekendSqls<T> andNotIn(String property, Iterable values) {
        return WeekendSqls.<T>custom().andNotIn(property, values);
    }

    public static <T> WeekendSqls<T> andNotIn(Fn<T, Object> fn, Iterable values) {
        return WeekendSqls.<T>custom().andNotIn(fn, values);
    }

    public static <T> WeekendSqls<T> andBetween(String property, Object value1, Object value2) {
        return WeekendSqls.<T>custom().andBetween(property, value1, value2);
    }

    public static <T> WeekendSqls<T> andBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return WeekendSqls.<T>custom().andBetween(fn, value1, value2);
    }

    public static <T> WeekendSqls<T> andNotBetween(String property, Object value1, Object value2) {
        return WeekendSqls.<T>custom().andNotBetween(property, value1, value2);
    }

    public static <T> WeekendSqls<T> andNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return WeekendSqls.<T>custom().andNotBetween(fn, value1, value2);
    }

    public static <T> WeekendSqls<T> andLike(String property, String value) {
        return WeekendSqls.<T>custom().andLike(property, value);
    }

    public static <T> WeekendSqls<T> andLike(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().andLike(fn, value);
    }

    public static <T> WeekendSqls<T> andNotLike(String property, String value) {
        return WeekendSqls.<T>custom().andNotLike(property, value);
    }

    public static <T> WeekendSqls<T> andNotLike(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().andNotLike(fn ,value);
    }

    public static <T> WeekendSqls<T> orIsNull(String property) {
        return WeekendSqls.<T>custom().orIsNull(property);
    }

    public static <T> WeekendSqls<T> orIsNull(Fn<T, Object> fn) {
        return WeekendSqls.<T>custom().orIsNull(fn);
    }

    public static <T> WeekendSqls<T> orIsNotNull(String property) {
        return WeekendSqls.<T>custom().orIsNotNull(property);
    }

    public static <T> WeekendSqls<T> orIsNotNull(Fn<T, Object> fn) {
        return WeekendSqls.<T>custom().orIsNotNull(fn);
    }

    public static <T> WeekendSqls<T> orEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().orEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> orEqualTo(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> orNotEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().orNotEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> orNotEqualTo(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orNotEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> orGreaterThan(String property, Object value) {
        return WeekendSqls.<T>custom().orGreaterThan(property, value);
    }

    public static <T> WeekendSqls<T> orGreaterThan(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orGreaterThan(fn, value);
    }

    public static <T> WeekendSqls<T> orGreaterThanOrEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().orGreaterThanOrEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> orGreaterThanOrEqualTo(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orGreaterThanOrEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> orLessThan(String property, Object value) {
        return WeekendSqls.<T>custom().orLessThan(property, value);
    }

    public static <T> WeekendSqls<T> orLessThan(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orLessThan(fn, value);
    }

    public static <T> WeekendSqls<T> orLessThanOrEqualTo(String property, Object value) {
        return WeekendSqls.<T>custom().orLessThanOrEqualTo(property, value);
    }

    public static <T> WeekendSqls<T> orLessThanOrEqualTo(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orLessThanOrEqualTo(fn, value);
    }

    public static <T> WeekendSqls<T> orIn(String property, Iterable values) {
        return WeekendSqls.<T>custom().orIn(property, values);
    }

    public static <T> WeekendSqls<T> orIn(Fn<T, Object> fn, Iterable values) {
        return WeekendSqls.<T>custom().orIn(fn, values);
    }

    public static <T> WeekendSqls<T> orNotIn(String property, Iterable values) {
        return WeekendSqls.<T>custom().orNotIn(property, values);
    }

    public static <T> WeekendSqls<T> orNotIn(Fn<T, Object> fn, Iterable values) {
        return WeekendSqls.<T>custom().orNotIn(fn, values);
    }

    public static <T> WeekendSqls<T> orBetween(String property, Object value1, Object value2) {
        return WeekendSqls.<T>custom().orBetween(property, value1, value2);
    }

    public static <T> WeekendSqls<T> orBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return WeekendSqls.<T>custom().orBetween(fn, value1, value2);
    }

    public static <T> WeekendSqls<T> orNotBetween(String property, Object value1, Object value2) {
        return WeekendSqls.<T>custom().orNotBetween(property, value1, value2);
    }

    public static <T> WeekendSqls<T> orNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return WeekendSqls.<T>custom().orNotBetween(fn, value1, value2);
    }

    public static <T> WeekendSqls<T> orLike(String property, String value) {
        return WeekendSqls.<T>custom().orLike(property, value);
    }

    public static <T> WeekendSqls<T> orLike(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orLike(fn, value);
    }

    public static <T> WeekendSqls<T> orNotLike(String property, String value) {
        return WeekendSqls.<T>custom().orNotLike(property, value);
    }

    public static <T> WeekendSqls<T> orNotLike(Fn<T, Object> fn, String value) {
        return WeekendSqls.<T>custom().orNotLike(fn, value);
    }

}
