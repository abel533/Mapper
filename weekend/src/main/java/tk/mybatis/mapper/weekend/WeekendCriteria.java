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

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.weekend.reflection.Reflections;

import java.util.Map;

/**
 * @author Frank
 */
public class WeekendCriteria<A, B> extends Criteria {
    protected WeekendCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
        super(propertyMap, exists, notNull);
    }

    public WeekendCriteria<A, B> andIsNull(Fn<A, B> fn) {
        this.andIsNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A, B> andIsNotNull(Fn<A, B> fn) {
        super.andIsNotNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A, B> andEqualTo(Fn<A, B> fn, Object value) {
        super.andEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andNotEqualTo(Fn<A, B> fn, Object value) {
        super.andNotEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andGreaterThan(Fn<A, B> fn, Object value) {
        super.andGreaterThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        super.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andLessThan(Fn<A, B> fn, Object value) {
        super.andLessThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        super.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andIn(Fn<A, B> fn, Iterable values) {
        super.andIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A, B> andNotIn(Fn<A, B> fn, Iterable values) {
        super.andNotIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A, B> andBetween(Fn<A, B> fn, Object value1, Object value2) {
        super.andBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A, B> andNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        super.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A, B> andLike(Fn<A, B> fn, String value) {
        super.andLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> andNotLike(Fn<A, B> fn, String value) {
        super.andNotLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orIsNull(Fn<A, B> fn) {
        super.orIsNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A, B> orIsNotNull(Fn<A, B> fn) {
        super.orIsNotNull(Reflections.fnToFieldName(fn));
        return this;
    }

    public WeekendCriteria<A, B> orEqualTo(Fn<A, B> fn, Object value) {
        super.orEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orNotEqualTo(Fn<A, B> fn, Object value) {
        super.orNotEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orGreaterThan(Fn<A, B> fn, Object value) {
        super.orGreaterThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        super.orGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orLessThan(Fn<A, B> fn, Object value) {
        super.orLessThan(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        super.orLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orIn(Fn<A, B> fn, Iterable values) {
        super.orIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A, B> orNotIn(Fn<A, B> fn, Iterable values) {
        super.orNotIn(Reflections.fnToFieldName(fn), values);
        return this;
    }

    public WeekendCriteria<A, B> orBetween(Fn<A, B> fn, Object value1, Object value2) {
        super.orBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A, B> orNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        super.orNotBetween(Reflections.fnToFieldName(fn), value1, value2);
        return this;
    }

    public WeekendCriteria<A, B> orLike(Fn<A, B> fn, String value) {
        super.orLike(Reflections.fnToFieldName(fn), value);
        return this;
    }

    public WeekendCriteria<A, B> orNotLike(Fn<A, B> fn, String value) {
        super.orNotLike(Reflections.fnToFieldName(fn), value);
        return this;
    }
}
