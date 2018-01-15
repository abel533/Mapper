package tk.mybatis.mapper.util;

import tk.mybatis.mapper.weekend.Fn;
import tk.mybatis.mapper.weekend.reflection.Reflections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuyi
 * @date 2017/11/18
 */
public class Sqls {
    private Criteria criteria;

    private Sqls() {
        this.criteria = new Criteria();
    }

    public static Sqls custom() {
        return new Sqls();
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Sqls andIsNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is null", "and"));
        return this;
    }

    public Sqls andIsNull(Fn fn) {
        return this.andIsNull(Reflections.fnToFieldName(fn));
    }

    public Sqls andIsNotNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is not null", "and"));
        return this;
    }

    public Sqls andIsNotNull(Fn fn) {
        return this.andIsNotNull(Reflections.fnToFieldName(fn));
    }

    public Sqls andEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "=", "and"));
        return this;
    }

    public Sqls andEqualTo(Fn fn, Object value) {
        return this.andEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<>", "and"));
        return this;
    }

    public Sqls andNotEqualTo(Fn fn, Object value) {
        return this.andNotEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">", "and"));
        return this;
    }

    public Sqls andGreaterThan(Fn fn, Object value) {
        return this.andGreaterThan(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">=", "and"));
        return this;
    }

    public Sqls andGreaterThanOrEqualTo(Fn fn, Object value) {
        return this.andGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }


    public Sqls andLessThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<", "and"));
        return this;
    }

    public Sqls andLessThan(Fn fn, Object value) {
        return this.andLessThan(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<=", "and"));
        return this;
    }

    public Sqls andLessThanOrEqualTo(Fn fn, Object value) {
        return this.andLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andIn(String property, Iterable values) {
        this.criteria.criterions.add(new Criterion(property, values, "in", "and"));
        return this;
    }

    public Sqls andIn(Fn fn, Iterable values) {
        return this.andIn(Reflections.fnToFieldName(fn), values);
    }

    public Sqls andNotIn(String property, Iterable values) {
        this.criteria.criterions.add(new Criterion(property, values, "not in", "and"));
        return this;
    }

    public Sqls andNotIn(Fn fn, Iterable values) {
        return this.andNotIn(Reflections.fnToFieldName(fn), values);
    }

    public Sqls andBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "and"));
        return this;
    }

    public Sqls andBetween(Fn fn, Object value1, Object value2) {
        return this.andBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public Sqls andNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "and"));
        return this;
    }

    public Sqls andNotBetween(Fn fn, Object value1, Object value2) {
        return this.andNotBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public Sqls andLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "like", "and"));
        return this;
    }

    public Sqls andLike(Fn fn, String value) {
        return this.andLike(Reflections.fnToFieldName(fn), value);
    }

    public Sqls andNotLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "not like", "and"));
        return this;
    }

    public Sqls andNotLike(Fn fn, String value) {
        return this.andNotLike(Reflections.fnToFieldName(fn), value);
    }


    public Sqls orIsNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is null", "or"));
        return this;
    }

    public Sqls orIsNull(Fn fn) {
        return this.orIsNull(Reflections.fnToFieldName(fn));
    }

    public Sqls orIsNotNull(String property) {
        this.criteria.criterions.add(new Criterion(property, "is not null", "or"));
        return this;
    }

    public Sqls orIsNotNull(Fn fn) {
        return this.orIsNotNull(Reflections.fnToFieldName(fn));
    }


    public Sqls orEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "=", "or"));
        return this;
    }

    public Sqls orEqualTo(Fn fn, String value) {
        return this.orEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orNotEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<>", "or"));
        return this;
    }

    public Sqls orNotEqualTo(Fn fn, String value) {
        return this.orNotEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orGreaterThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">", "or"));
        return this;
    }

    public Sqls orGreaterThan(Fn fn, String value) {
        return this.orGreaterThan(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, ">=", "or"));
        return this;
    }

    public Sqls orGreaterThanOrEqualTo(Fn fn, String value) {
        return this.orGreaterThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orLessThan(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<", "or"));
        return this;
    }

    public Sqls orLessThan(Fn fn, String value) {
        return this.orLessThan(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orLessThanOrEqualTo(String property, Object value) {
        this.criteria.criterions.add(new Criterion(property, value, "<=", "or"));
        return this;
    }

    public Sqls orLessThanOrEqualTo(Fn fn, String value) {
        return this.orLessThanOrEqualTo(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orIn(String property, Iterable values) {
        this.criteria.criterions.add(new Criterion(property, values, "in", "or"));
        return this;
    }

    public Sqls orIn(Fn fn, Iterable values) {
        return this.orIn(Reflections.fnToFieldName(fn), values);
    }

    public Sqls orNotIn(String property, Iterable values) {
        this.criteria.criterions.add(new Criterion(property, values, "not in", "or"));
        return this;
    }

    public Sqls orNotIn(Fn fn, Iterable values) {
        return this.orNotIn(Reflections.fnToFieldName(fn), values);
    }

    public Sqls orBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "between", "or"));
        return this;
    }

    public Sqls orBetween(Fn fn, Object value1, Object value2) {
        return this.orBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public Sqls orNotBetween(String property, Object value1, Object value2) {
        this.criteria.criterions.add(new Criterion(property, value1, value2, "not between", "or"));
        return this;
    }

    public Sqls orNotBetween(Fn fn, Object value1, Object value2) {
        return this.orNotBetween(Reflections.fnToFieldName(fn), value1, value2);
    }

    public Sqls orLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "like", "or"));
        return this;
    }

    public Sqls orLike(Fn fn, String value) {
        return this.orLike(Reflections.fnToFieldName(fn), value);
    }

    public Sqls orNotLike(String property, String value) {
        this.criteria.criterions.add(new Criterion(property, value, "not like", "or"));
        return this;
    }

    public Sqls orNotLike(Fn fn, String value) {
        return this.orNotLike(Reflections.fnToFieldName(fn), value);
    }

    public static class Criteria {
        private String andOr;
        private List<Criterion> criterions;

        public Criteria() {
            this.criterions = new ArrayList<Criterion>(2);
        }

        public List<Criterion> getCriterions() {
            return criterions;
        }

        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }
    }

    public static class Criterion {
        private String property;
        private Object value;
        private Object secondValue;
        private String condition;
        private String andOr;

        public Criterion(String property, String condition, String andOr) {
            this.property = property;
            this.condition = condition;
            this.andOr = andOr;
        }


        public Criterion(String property, Object value, String condition, String andOr) {
            this.property = property;
            this.value = value;
            this.condition = condition;
            this.andOr = andOr;
        }

        public Criterion(String property, Object value1, Object value2, String condition, String andOr) {
            this.property = property;
            this.value = value1;
            this.secondValue = value2;
            this.condition = condition;
            this.andOr = andOr;
        }

        public String getProperty() {
            return property;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public Object[] getValues() {
            if (value != null) {
                if (secondValue != null) {
                    return new Object[]{value, secondValue};
                } else {
                    return new Object[]{value};
                }
            } else {
                return new Object[]{};
            }
        }

        public String getCondition() {
            return condition;
        }

        public String getAndOr() {
            return andOr;
        }
    }
}
