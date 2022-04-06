package tk.mybatis.mapper.weekend;

import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.weekend.reflection.Reflections;

import java.util.Optional;

/**
 * sql 条件语句
 *
 * @author Cheng.Wei
 * @date 2019-04-15 10:26
 */
public class SqlCriteriaHelper<T> implements tk.mybatis.mapper.entity.SqlsCriteria {
    private Sqls.Criteria criteria;

    private SqlCriteriaHelper() {
        this.criteria = new Sqls.Criteria();
    }

    public static <T> SqlCriteriaHelper<T> custom(Class<T> clazz) {
        return new SqlCriteriaHelper<T>();
    }

    /**
     * AND column IS NULL
     *
     * @param fn
     * @return
     */
    public SqlCriteriaHelper<T> andIsNull(Fn<T, Object> fn) {
        this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is null", "and"));
        return this;
    }


    /**
     * AND column IS NOT NULL
     *
     * @param fn
     * @return
     */
    public SqlCriteriaHelper<T> andIsNotNull(Fn<T, Object> fn) {
        this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is not null", "and"));
        return this;
    }

    /**
     * AND column = value
     * 当value=null则不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andEqualTo(Fn<T, Object> fn, Object value) {
        return this.andEqualTo(fn, value, false);
    }

    /**
     * AND column = value
     *
     * @param fn
     * @param value
     * @param required false 当value=null 则不参与查询 ;
     *                 true 当value = null 则转 is null 查询： AND column is null
     * @return
     */
    public SqlCriteriaHelper<T> andEqualTo(Fn<T, Object> fn, Object value, boolean required) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "=", "and"));
        } else {
            if (required) {
                // null属性查询 转 is null
                this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is null", "and"));
            }
        }
        return this;
    }

    /**
     * AND column != value
     * 默认 value=null 则不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andNotEqualTo(Fn<T, Object> fn, Object value) {
        return this.andNotEqualTo(fn, value, false);
    }

    /**
     * AND column != value
     *
     * @param fn
     * @param value
     * @param required false 当value=null 则不参与查询 ;
     *                 true 当value = null 则转 is not null 查询 ： AND column is not null
     * @return
     */
    public SqlCriteriaHelper<T> andNotEqualTo(Fn<T, Object> fn, Object value, boolean required) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<>", "and"));
        } else {
            if (required) {
                //转非空查询
                this.andIsNotNull(fn);
            }
        }
        return this;
    }

    /**
     * AND column > value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andGreaterThan(Fn<T, Object> fn, Object value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, ">", "and"));
        }
        return this;
    }

    /**
     * AND  column >= value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andGreaterThanOrEqualTo(Fn<T, Object> fn, Object value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, ">=", "and"));
        }
        return this;
    }

    /**
     * AND  column < value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andLessThan(Fn<T, Object> fn, Object value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<", "and"));
        }
        return this;
    }

    /**
     * AND  column <= value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andLessThanOrEqualTo(Fn<T, Object> fn, Object value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<=", "and"));
        }
        return this;
    }

    /**
     * AND  column IN (#{item.value})
     * 当 values = null 则当前属性不参与查询
     *
     * @param fn
     * @param values
     * @return
     */
    public SqlCriteriaHelper<T> andIn(Fn<T, Object> fn, Iterable values) {
        if (Optional.ofNullable(values).isPresent() && values.iterator().hasNext()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), values, "in", "and"));
        }
        return this;
    }

    /**
     * AND  column NOT IN (#{item.value})
     * 当 values = null 则当前属性不参与查询
     *
     * @param fn
     * @param values
     * @return
     */
    public SqlCriteriaHelper<T> andNotIn(Fn<T, Object> fn, Iterable values) {
        if (Optional.ofNullable(values).isPresent() && values.iterator().hasNext()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), values, "not in", "and"));
        }
        return this;
    }

    /**
     * AND  column BETWEEN  value1 AND value2
     * 当 value1 或 value2 为空 则当前属性不参与查询
     *
     * @param fn
     * @param value1
     * @param value2
     * @return
     */
    public SqlCriteriaHelper<T> andBetween(Fn<T, Object> fn, Object value1, Object value2) {
        if (Optional.ofNullable(value1).isPresent() && Optional.ofNullable(value2).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value1, value2, "between", "and"));
        }
        return this;
    }

    /**
     * AND column  NOT BETWEEN value1 AND value2
     * 当 value1 或 value2 为空 则当前属性不参与查询
     *
     * @param fn
     * @param value1
     * @param value2
     * @return
     */
    public SqlCriteriaHelper<T> andNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        if (Optional.ofNullable(value1).isPresent() && Optional.ofNullable(value2).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value1, value2, "not between", "and"));
        }
        return this;
    }

    /**
     * AND column LIKE %value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andLike(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "and"));
        }
        return this;
    }


    /**
     * AND column LIKE %value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andLikeLeft(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value;
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "and"));
        }
        return this;
    }

    /**
     * AND column LIKE value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andLikeRight(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "and"));
        }
        return this;
    }

    /**
     * AND column NOT LIKE %value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andNotLike(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "and"));
        }
        return this;
    }

    /**
     * AND column NOT LIKE %value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andNotLikeLeft(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "and"));
        }
        return this;
    }

    /**
     * AND column NOT LIKE value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> andNotLikeRight(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "and"));
        }
        return this;
    }

    /**
     * OR column IS NULL
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @return
     */
    public SqlCriteriaHelper<T> orIsNull(Fn<T, Object> fn) {
        this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is null", "or"));
        return this;
    }

    /**
     * OR column IS NOT NULL
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @return
     */
    public SqlCriteriaHelper<T> orIsNotNull(Fn<T, Object> fn) {
        this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is not null", "or"));
        return this;
    }


    /**
     * OR column = value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orEqualTo(Fn<T, Object> fn, Object value) {
        return this.orEqualTo(fn, value, false);
    }

    /**
     * OR column = value
     * 当request = true 且  value = null时 转 #{@link #orIsNull(Fn)}
     *
     * @param fn
     * @param value
     * @param required
     * @return
     */
    public SqlCriteriaHelper<T> orEqualTo(Fn<T, Object> fn, Object value, boolean required) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "=", "or"));
        } else {
            if (required) {
                //转 or null
                this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), "is null", "or"));
            }
        }
        return this;
    }

    /**
     * OR column <> value
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orNotEqualTo(Fn<T, Object> fn, Object value) {
        return this.orNotEqualTo(fn, value, false);
    }

    /**
     * OR column <> value
     * 当request = true 且  value = null时 转 #{@link #orIsNotNull(Fn)}
     *
     * @param fn
     * @param value
     * @param required
     * @return
     */
    public SqlCriteriaHelper<T> orNotEqualTo(Fn<T, Object> fn, Object value, boolean required) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<>", "or"));
        } else {
            if (required) {
                // 转 or is not null
                this.orIsNotNull(fn);
            }
        }
        return this;
    }

    /**
     * OR column > value
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orGreaterThan(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, ">", "or"));
        }
        return this;
    }

    /**
     * OR column >= value
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orGreaterThanOrEqualTo(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, ">=", "or"));
        }
        return this;
    }

    /**
     * OR column < value
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orLessThan(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<", "or"));
        }
        return this;
    }

    /**
     * OR column <= value
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orLessThanOrEqualTo(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "<=", "or"));
        }
        return this;
    }

    /**
     * OR column IN (#{item.value})
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param values
     * @return
     */
    public SqlCriteriaHelper<T> orIn(Fn<T, Object> fn, Iterable values) {
        if (Optional.ofNullable(values).isPresent() && values.iterator().hasNext()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), values, "in", "or"));
        }
        return this;
    }

    /**
     * OR column NOT IN (#{item.value})
     * 当value = null 则当前属性不参与查询
     *
     * @param fn
     * @param values
     * @return
     */
    public SqlCriteriaHelper<T> orNotIn(Fn<T, Object> fn, Iterable values) {
        if (Optional.ofNullable(values).isPresent() && values.iterator().hasNext()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), values, "not in", "or"));
        }
        return this;
    }

    /**
     * OR column BETWEEN  value1 AND value2
     * 当 value1 或 value2 为空 则当前属性不参与查询
     *
     * @param fn
     * @param value1
     * @param value2
     * @return
     */
    public SqlCriteriaHelper<T> orBetween(Fn<T, Object> fn, Object value1, Object value2) {
        if (Optional.ofNullable(value1).isPresent() && Optional.ofNullable(value2).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value1, value2, "between", "or"));
        }
        return this;
    }

    /**
     * OR column NOT BETWEEN  value1 AND value2
     * 当 value1 或 value2 为空 则当前属性不参与查询
     *
     * @param fn
     * @param value1
     * @param value2
     * @return
     */
    public SqlCriteriaHelper<T> orNotBetween(Fn<T, Object> fn, Object value1, Object value2) {
        if (Optional.ofNullable(value1).isPresent() && Optional.ofNullable(value2).isPresent()) {
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value1, value2, "not between", "or"));
        }
        return this;
    }


    /**
     * OR column LIKE value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orLike(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "or"));
        }
        return this;
    }


    /**
     * OR column LIKE %value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orLikeLeft(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value;
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "or"));
        }
        return this;
    }


    /**
     * OR column LIKE value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orLikeRight(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "like", "or"));
        }
        return this;
    }


    /**
     * OR column NOT LIKE value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orNotLike(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "or"));
        }
        return this;
    }


    /**
     * OR column NOT LIKE %value
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orNotLikeLeft(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = "%" + value;
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "or"));
        }
        return this;
    }

    /**
     * OR column NOT LIKE value%
     * 当 value = null 则当前属性不参与查询
     *
     * @param fn
     * @param value
     * @return
     */
    public SqlCriteriaHelper<T> orNotLikeRight(Fn<T, Object> fn, String value) {
        if (Optional.ofNullable(value).isPresent()) {
            value = value + "%";
            this.criteria.getCriterions().add(new Sqls.Criterion(Reflections.fnToFieldName(fn), value, "not like", "or"));
        }
        return this;
    }


    @Override
    public Sqls.Criteria getCriteria() {
        return criteria;
    }
}
