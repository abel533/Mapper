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

import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.util.Sqls.Criteria;
import tk.mybatis.mapper.util.Sqls.Criterion;
import tk.mybatis.mapper.weekend.reflection.Reflections;

/**
 * @author XuYin
 */
public class WeekendSqls<T> implements tk.mybatis.mapper.entity.SqlsCriteria {
    private Criteria criteria;

    private WeekendSqls() {
        this.criteria = new Sqls.Criteria();
    }

    public static <T> WeekendSqls<T> custom() {
        return new WeekendSqls<T>();
    }

    public WeekendSqls<T> andIsNull(String property) {
        this.criteria.getCriterions().add(new Criterion(property, "is null", "and"));
        return this;
    }

    public WeekendSqls<T> andIsNull(Fn<T, Object> fn) {
        return this.andIsNull(Reflections.fnToFieldName(fn));
    }

    public WeekendSqls<T> andIsNotNull(String property) {
        this.criteria.getCriterions().add(new Criterion(property, "is not null", "and"));
        return this;
    }

    public WeekendSqls<T> andIsNotNull(Fn<T, Object> fn) {
        return this.andIsNotNull(Reflections.fnToFieldName(fn));
    }

    public WeekendSqls<T> andEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "=", "and"));
        return this;
    }

    public WeekendSqls<T> andEqualTo(Fn<T, Object> fn, Object value) {
        return this.andEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andNotEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<>", "and"));
        return this;
    }

    public WeekendSqls<T> andNotEqualTo(Fn<T, Object> fn, Object value) {
        return this.andNotEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andGreaterThan(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, ">", "and"));
        return this;
    }

    public WeekendSqls<T> andGreaterThan(Fn<T, Object> fn, Object value) {
        return this.andGreaterThan(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, ">=", "and"));
        return this;
    }

    public WeekendSqls<T> andGreaterThanOrEqualTo(Fn<T, Object> fn, Object value) {
        return this.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andLessThan(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<", "and"));
        return this;
    }

    public WeekendSqls<T> andLessThan(Fn<T, Object> fn, Object value) {
        return this.andLessThan(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andLessThanOrEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<=", "and"));
        return this;
    }

    public WeekendSqls<T> andLessThanOrEqualTo(Fn<T, Object> fn, Object value) {
        return this.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andIn(String property, Iterable values) {
        this.criteria.getCriterions().add(new Criterion(property, values, "in", "and"));
        return this;
    }

    public WeekendSqls<T> andIn(Fn<T, Object> fn, Iterable values) {
        return this.andIn(Reflections.fnToFieldName(fn), values);
    }

    public WeekendSqls<T> andNotIn(String property, Iterable values) {
        this.criteria.getCriterions().add(new Criterion(property, values, "not in", "and"));
        return this;
    }

    public WeekendSqls<T> andNotIn(Fn<T, Object> fn, Iterable values) {
        return this.andNotIn(Reflections.fnToFieldName(fn), values);
    }

    public WeekendSqls<T> andBetween(String property, Object value1, Object value2) {
        this.criteria.getCriterions().add(new Criterion(property, value1, value2, "between", "and"));
        return this;
    }

    public WeekendSqls<T> andBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return this.andBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public WeekendSqls<T> andNotBetween(String property, Object value1, Object value2) {
        this.criteria.getCriterions().add(new Criterion(property, value1, value2, "not between", "and"));
        return this;
    }

    public WeekendSqls<T> andNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return this.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public WeekendSqls<T> andLike(String property, String value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "like", "and"));
        return this;
    }

    public WeekendSqls<T> andLike(Fn<T, Object> fn, String value) {
        return this.andLike(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> andNotLike(String property, String value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "not like", "and"));
        return this;
    }

    public WeekendSqls<T> andNotLike(Fn<T, Object> fn, String value) {
        return this.andNotLike(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orIsNull(String property) {
        this.criteria.getCriterions().add(new Criterion(property, "is null", "or"));
        return this;
    }

    public WeekendSqls<T> orIsNull(Fn<T, Object> fn) {
        return this.orIsNull(Reflections.fnToFieldName(fn));
    }

    public WeekendSqls<T> orIsNotNull(String property) {
        this.criteria.getCriterions().add(new Criterion(property, "is not null", "or"));
        return this;
    }

    public WeekendSqls<T> orIsNotNull(Fn<T, Object> fn) {
        return this.orIsNotNull(Reflections.fnToFieldName(fn));
    }

    public WeekendSqls<T> orEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "=", "or"));
        return this;
    }

    public WeekendSqls<T> orEqualTo(Fn<T, Object> fn, String value) {
        return this.orEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orNotEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<>", "or"));
        return this;
    }

    public WeekendSqls<T> orNotEqualTo(Fn<T, Object> fn, String value) {
        return this.orNotEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orGreaterThan(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, ">", "or"));
        return this;
    }

    public WeekendSqls<T> orGreaterThan(Fn<T, Object> fn, String value) {
        return this.orGreaterThan(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, ">=", "or"));
        return this;
    }

    public WeekendSqls<T> orGreaterThanOrEqualTo(Fn<T, Object> fn, String value) {
        return this.orGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orLessThan(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<", "or"));
        return this;
    }

    public WeekendSqls<T> orLessThan(Fn<T, Object> fn, String value) {
        return this.orLessThan(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orLessThanOrEqualTo(String property, Object value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "<=", "or"));
        return this;
    }

    public WeekendSqls<T> orLessThanOrEqualTo(Fn<T, Object> fn, String value) {
        return this.orLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orIn(String property, Iterable values) {
        this.criteria.getCriterions().add(new Criterion(property, values, "in", "or"));
        return this;
    }

    public WeekendSqls<T> orIn(Fn<T, Object> fn, Iterable values) {
        return this.orIn(Reflections.fnToFieldName(fn), values);
    }

    public WeekendSqls<T> orNotIn(String property, Iterable values) {
        this.criteria.getCriterions().add(new Criterion(property, values, "not in", "or"));
        return this;
    }

    public WeekendSqls<T> orNotIn(Fn<T, Object> fn, Iterable values) {
        return this.orNotIn(Reflections.fnToFieldName(fn), values);
    }

    public WeekendSqls<T> orBetween(String property, Object value1, Object value2) {
        this.criteria.getCriterions().add(new Criterion(property, value1, value2, "between", "or"));
        return this;
    }

    public WeekendSqls<T> orBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return this.orBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public WeekendSqls<T> orNotBetween(String property, Object value1, Object value2) {
        this.criteria.getCriterions().add(new Criterion(property, value1, value2, "not between", "or"));
        return this;
    }

    public WeekendSqls<T> orNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        return this.orNotBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public WeekendSqls<T> orLike(String property, String value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "like", "or"));
        return this;
    }

    public WeekendSqls<T> orLike(Fn<T, Object> fn, String value) {
        return this.orLike(Reflections.fnToFieldName(fn), value);
    }

    public WeekendSqls<T> orNotLike(String property, String value) {
        this.criteria.getCriterions().add(new Criterion(property, value, "not like", "or"));
        return this;
    }

    public WeekendSqls<T> orNotLike(Fn<T, Object> fn, String value) {
        return this.orNotLike(Reflections.fnToFieldName(fn), value);
    }

    @Override
    public Criteria getCriteria() {
        return criteria;
    }
}
