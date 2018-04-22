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

package tk.mybatis.mapper.entity;

import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;
import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;

/**
 * 通用的Example查询对象
 *
 * @author liuzh
 */
public class Example implements IDynamicTableName {
    protected String orderByClause;

    protected boolean distinct;

    protected boolean exists;

    protected boolean notNull;

    protected boolean forUpdate;

    //查询字段
    protected Set<String> selectColumns;

    //排除的查询字段
    protected Set<String> excludeColumns;

    protected String countColumn;

    protected List<Criteria> oredCriteria;

    protected Class<?> entityClass;

    protected EntityTable               table;
    //属性和列对应
    protected Map<String, EntityColumn> propertyMap;
    //动态表名
    protected String                    tableName;

    protected OrderBy ORDERBY;

    /**
     * 默认exists为true
     *
     * @param entityClass
     */
    public Example(Class<?> entityClass) {
        this(entityClass, true);
    }

    /**
     * 带exists参数的构造方法，默认notNull为false，允许为空
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     */
    public Example(Class<?> entityClass, boolean exists) {
        this(entityClass, exists, false);
    }

    /**
     * 带exists参数的构造方法
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     * @param notNull     - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
     */
    public Example(Class<?> entityClass, boolean exists, boolean notNull) {
        this.exists = exists;
        this.notNull = notNull;
        oredCriteria = new ArrayList<Criteria>();
        this.entityClass = entityClass;
        table = EntityHelper.getEntityTable(entityClass);
        //根据李领北建议修改#159
        propertyMap = table.getPropertyMap();
        this.ORDERBY = new OrderBy(this, propertyMap);
    }


    private Example(Builder builder) {
        this.exists = builder.exists;
        this.notNull = builder.notNull;
        this.distinct = builder.distinct;
        this.entityClass = builder.entityClass;
        this.propertyMap = builder.propertyMap;
        this.selectColumns = builder.selectColumns;
        this.excludeColumns = builder.excludeColumns;
        this.oredCriteria = builder.exampleCriterias;
        this.forUpdate = builder.forUpdate;
        this.tableName = builder.tableName;

        if (!StringUtil.isEmpty(builder.orderByClause.toString())) {
            this.orderByClause = builder.orderByClause.toString();
        }
    }

    public static Builder builder(Class<?> entityClass) {
        return new Builder(entityClass);
    }

    public OrderBy orderBy(String property) {
        this.ORDERBY.orderBy(property);
        return this.ORDERBY;
    }

    /**
     * 排除查询字段，优先级低于 selectProperties
     *
     * @param properties 属性名的可变参数
     * @return
     */
    public Example excludeProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.excludeColumns == null) {
                this.excludeColumns = new LinkedHashSet<String>();
            }
            for (String property : properties) {
                if (propertyMap.containsKey(property)) {
                    this.excludeColumns.add(propertyMap.get(property).getColumn());
                } else {
                    throw new MapperException("类 " + entityClass.getSimpleName() + " 不包含属性 \'" + property + "\'，或该属性被@Transient注释！");
                }
            }
        }
        return this;
    }

    /**
     * 指定要查询的属性列 - 这里会自动映射到表字段
     *
     * @param properties
     * @return
     */
    public Example selectProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet<String>();
            }
            for (String property : properties) {
                if (propertyMap.containsKey(property)) {
                    this.selectColumns.add(propertyMap.get(property).getColumn());
                } else {
                    throw new MapperException("类 " + entityClass.getSimpleName() + " 不包含属性 \'" + property + "\'，或该属性被@Transient注释！");
                }
            }
        }
        return this;
    }

    public void or(Criteria criteria) {
        criteria.setAndOr("or");
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        criteria.setAndOr("or");
        oredCriteria.add(criteria);
        return criteria;
    }

    public void and(Criteria criteria) {
        criteria.setAndOr("and");
        oredCriteria.add(criteria);
    }

    public Criteria and() {
        Criteria criteria = createCriteriaInternal();
        criteria.setAndOr("and");
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            criteria.setAndOr("and");
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria(propertyMap, exists, notNull);
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public static class OrderBy {
        //属性和列对应
        protected Map<String, EntityColumn> propertyMap;
        private   Example                   example;
        private   Boolean                   isProperty;

        public OrderBy(Example example, Map<String, EntityColumn> propertyMap) {
            this.example = example;
            this.propertyMap = propertyMap;
        }

        private String property(String property) {
            if (StringUtil.isEmpty(property) || StringUtil.isEmpty(property.trim())) {
                throw new MapperException("接收的property为空！");
            }
            property = property.trim();
            if (!propertyMap.containsKey(property)) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            }
            return propertyMap.get(property).getColumn();
        }

        public OrderBy orderBy(String property) {
            String column = property(property);
            if (column == null) {
                isProperty = false;
                return this;
            }
            if (StringUtil.isNotEmpty(example.getOrderByClause())) {
                example.setOrderByClause(example.getOrderByClause() + "," + column);
            } else {
                example.setOrderByClause(column);
            }
            isProperty = true;
            return this;
        }

        public OrderBy desc() {
            if (isProperty) {
                example.setOrderByClause(example.getOrderByClause() + " DESC");
                isProperty = false;
            }
            return this;
        }

        public OrderBy asc() {
            if (isProperty) {
                example.setOrderByClause(example.getOrderByClause() + " ASC");
                isProperty = false;
            }
            return this;
        }
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion>           criteria;
        //字段是否必须存在
        protected boolean                   exists;
        //值是否不能为空
        protected boolean                   notNull;
        //连接条件
        protected String                    andOr;
        //属性和列对应
        protected Map<String, EntityColumn> propertyMap;

        protected GeneratedCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super();
            this.exists = exists;
            this.notNull = notNull;
            criteria = new ArrayList<Criterion>();
            this.propertyMap = propertyMap;
        }

        private String column(String property) {
            if (propertyMap.containsKey(property)) {
                return propertyMap.get(property).getColumn();
            } else if (exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (propertyMap.containsKey(property)) {
                return property;
            } else if (exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new MapperException("Value for condition cannot be null");
            }
            if (condition.startsWith("null")) {
                return;
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                if (notNull) {
                    throw new MapperException("Value for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                if (notNull) {
                    throw new MapperException("Between values for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addOrCriterion(String condition) {
            if (condition == null) {
                throw new MapperException("Value for condition cannot be null");
            }
            if (condition.startsWith("null")) {
                return;
            }
            criteria.add(new Criterion(condition, true));
        }

        protected void addOrCriterion(String condition, Object value, String property) {
            if (value == null) {
                if (notNull) {
                    throw new MapperException("Value for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            criteria.add(new Criterion(condition, value, true));
        }

        protected void addOrCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                if (notNull) {
                    throw new MapperException("Between values for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            criteria.add(new Criterion(condition, value1, value2, true));
        }

        public Criteria andIsNull(String property) {
            addCriterion(column(property) + " is null");
            return (Criteria) this;
        }

        public Criteria andIsNotNull(String property) {
            addCriterion(column(property) + " is not null");
            return (Criteria) this;
        }

        public Criteria andEqualTo(String property, Object value) {
            addCriterion(column(property) + " =", value, property(property));
            return (Criteria) this;
        }

        public Criteria andNotEqualTo(String property, Object value) {
            addCriterion(column(property) + " <>", value, property(property));
            return (Criteria) this;
        }

        public Criteria andGreaterThan(String property, Object value) {
            addCriterion(column(property) + " >", value, property(property));
            return (Criteria) this;
        }

        public Criteria andGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " >=", value, property(property));
            return (Criteria) this;
        }

        public Criteria andLessThan(String property, Object value) {
            addCriterion(column(property) + " <", value, property(property));
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " <=", value, property(property));
            return (Criteria) this;
        }

        public Criteria andIn(String property, Iterable values) {
            addCriterion(column(property) + " in", values, property(property));
            return (Criteria) this;
        }

        public Criteria andNotIn(String property, Iterable values) {
            addCriterion(column(property) + " not in", values, property(property));
            return (Criteria) this;
        }

        public Criteria andBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria andNotBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " not between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria andLike(String property, String value) {
            addCriterion(column(property) + "  like", value, property(property));
            return (Criteria) this;
        }

        public Criteria andNotLike(String property, String value) {
            addCriterion(column(property) + "  not like", value, property(property));
            return (Criteria) this;
        }

        /**
         * 手写条件
         *
         * @param condition 例如 "length(countryname)<5"
         * @return
         */
        public Criteria andCondition(String condition) {
            addCriterion(condition);
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition 例如 "length(countryname)="
         * @param value     例如 5
         * @return
         */
        public Criteria andCondition(String condition, Object value) {
            criteria.add(new Criterion(condition, value));
            return (Criteria) this;
        }

        /**
         * 将此对象的不为空的字段参数作为相等查询条件
         *
         * @param param 参数对象
         * @author Bob {@link}0haizhu0@gmail.com
         * @Date 2015年7月17日 下午12:48:08
         */
        public Criteria andEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        andEqualTo(property, value);
                    }
                }
            }
            return (Criteria) this;
        }

        /**
         * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
         *
         * @param param 参数对象
         */
        public Criteria andAllEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        andEqualTo(property, value);
                    } else {
                        andIsNull(property);
                    }
                }
            }
            return (Criteria) this;
        }

        public Criteria orIsNull(String property) {
            addOrCriterion(column(property) + " is null");
            return (Criteria) this;
        }

        public Criteria orIsNotNull(String property) {
            addOrCriterion(column(property) + " is not null");
            return (Criteria) this;
        }

        public Criteria orEqualTo(String property, Object value) {
            addOrCriterion(column(property) + " =", value, property(property));
            return (Criteria) this;
        }

        public Criteria orNotEqualTo(String property, Object value) {
            addOrCriterion(column(property) + " <>", value, property(property));
            return (Criteria) this;
        }

        public Criteria orGreaterThan(String property, Object value) {
            addOrCriterion(column(property) + " >", value, property(property));
            return (Criteria) this;
        }

        public Criteria orGreaterThanOrEqualTo(String property, Object value) {
            addOrCriterion(column(property) + " >=", value, property(property));
            return (Criteria) this;
        }

        public Criteria orLessThan(String property, Object value) {
            addOrCriterion(column(property) + " <", value, property(property));
            return (Criteria) this;
        }

        public Criteria orLessThanOrEqualTo(String property, Object value) {
            addOrCriterion(column(property) + " <=", value, property(property));
            return (Criteria) this;
        }

        public Criteria orIn(String property, Iterable values) {
            addOrCriterion(column(property) + " in", values, property(property));
            return (Criteria) this;
        }

        public Criteria orNotIn(String property, Iterable values) {
            addOrCriterion(column(property) + " not in", values, property(property));
            return (Criteria) this;
        }

        public Criteria orBetween(String property, Object value1, Object value2) {
            addOrCriterion(column(property) + " between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria orNotBetween(String property, Object value1, Object value2) {
            addOrCriterion(column(property) + " not between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria orLike(String property, String value) {
            addOrCriterion(column(property) + "  like", value, property(property));
            return (Criteria) this;
        }

        public Criteria orNotLike(String property, String value) {
            addOrCriterion(column(property) + "  not like", value, property(property));
            return (Criteria) this;
        }

        /**
         * 手写条件
         *
         * @param condition 例如 "length(countryname)<5"
         * @return
         */
        public Criteria orCondition(String condition) {
            addOrCriterion(condition);
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition 例如 "length(countryname)="
         * @param value     例如 5
         * @return
         */
        public Criteria orCondition(String condition, Object value) {
            criteria.add(new Criterion(condition, value, true));
            return (Criteria) this;
        }

        /**
         * 将此对象的不为空的字段参数作为相等查询条件
         *
         * @param param 参数对象
         * @author Bob {@link}0haizhu0@gmail.com
         * @Date 2015年7月17日 下午12:48:08
         */
        public Criteria orEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        orEqualTo(property, value);
                    }
                }
            }
            return (Criteria) this;
        }

        /**
         * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
         *
         * @param param 参数对象
         */
        public Criteria orAllEqualTo(Object param) {
            MetaObject metaObject = MetaObjectUtil.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        orEqualTo(property, value);
                    } else {
                        orIsNull(property);
                    }
                }
            }
            return (Criteria) this;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super(propertyMap, exists, notNull);
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private String andOr;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        protected Criterion(String condition) {
            this(condition, false);
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this(condition, value, typeHandler, false);
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null, false);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this(condition, value, secondValue, typeHandler, false);
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null, false);
        }

        protected Criterion(String condition, boolean isOr) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
            this.andOr = isOr ? "or" : "and";
        }

        protected Criterion(String condition, Object value, String typeHandler, boolean isOr) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            this.andOr = isOr ? "or" : "and";
            if (value instanceof Collection<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value, boolean isOr) {
            this(condition, value, null, isOr);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler, boolean isOr) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
            this.andOr = isOr ? "or" : "and";
        }

        protected Criterion(String condition, Object value, Object secondValue, boolean isOr) {
            this(condition, value, secondValue, null, isOr);
        }

        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }

        public String getCondition() {
            return condition;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public Object getValue() {
            return value;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }
    }

    public static class Builder {
        private final Class<?>                  entityClass;
        protected     EntityTable               table;
        //属性和列对应
        protected     Map<String, EntityColumn> propertyMap;
        private       StringBuilder             orderByClause;
        private       boolean                   distinct;
        private       boolean                   exists;
        private       boolean                   notNull;
        private       boolean                   forUpdate;
        //查询字段
        private       Set<String>               selectColumns;
        //排除的查询字段
        private       Set<String>               excludeColumns;
        private       String                    countColumn;
        private       List<Sqls.Criteria>       sqlsCriteria;
        //动态表名
        private       List<Example.Criteria>    exampleCriterias;
        //动态表名
        private       String                    tableName;

        public Builder(Class<?> entityClass) {
            this(entityClass, true);
        }

        public Builder(Class<?> entityClass, boolean exists) {
            this(entityClass, exists, false);
        }

        public Builder(Class<?> entityClass, boolean exists, boolean notNull) {
            this.entityClass = entityClass;
            this.exists = exists;
            this.notNull = notNull;
            this.orderByClause = new StringBuilder();
            this.table = EntityHelper.getEntityTable(entityClass);
            this.propertyMap = table.getPropertyMap();
            this.sqlsCriteria = new ArrayList<Sqls.Criteria>(2);
        }

        public Builder distinct() {
            return setDistinct(true);
        }

        public Builder forUpdate() {
            return setForUpdate(true);
        }

        public Builder selectDistinct(String... properties) {
            select(properties);
            this.distinct = true;
            return this;
        }

        public Builder select(String... properties) {
            if (properties != null && properties.length > 0) {
                if (this.selectColumns == null) {
                    this.selectColumns = new LinkedHashSet<String>();
                }
                for (String property : properties) {
                    if (this.propertyMap.containsKey(property)) {
                        this.selectColumns.add(propertyMap.get(property).getColumn());
                    } else {
                        throw new MapperException("当前实体类不包含名为" + property + "的属性!");
                    }
                }
            }
            return this;
        }

        public Builder notSelect(String... properties) {
            if (properties != null && properties.length > 0) {
                if (this.excludeColumns == null) {
                    this.excludeColumns = new LinkedHashSet<String>();
                }
                for (String property : properties) {
                    if (propertyMap.containsKey(property)) {
                        this.excludeColumns.add(propertyMap.get(property).getColumn());
                    } else {
                        throw new MapperException("当前实体类不包含名为" + property + "的属性!");
                    }
                }
            }
            return this;
        }

        public Builder from(String tableName) {
            return setTableName(tableName);
        }

        public Builder where(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder where(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder andWhere(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder andWhere(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("and");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder orWhere(Sqls sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("or");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder orWhere(SqlsCriteria sqls) {
            Sqls.Criteria criteria = sqls.getCriteria();
            criteria.setAndOr("or");
            this.sqlsCriteria.add(criteria);
            return this;
        }

        public Builder orderBy(String... properties) {
            return orderByAsc(properties);
        }

        public Builder orderByAsc(String... properties) {
            contactOrderByClause(" Asc", properties);
            return this;
        }

        public Builder orderByDesc(String... properties) {
            contactOrderByClause(" Desc", properties);
            return this;
        }

        private void contactOrderByClause(String order, String... properties) {
            StringBuilder columns = new StringBuilder();
            for (String property : properties) {
                String column;
                if ((column = propertyforOderBy(property)) != null) {
                    columns.append(",").append(column);
                }
            }
            columns.append(order);
            if (columns.length() > 0) {
                orderByClause.append(columns);
            }
        }

        public Example build() {
            this.exampleCriterias = new ArrayList<Criteria>();
            for (Sqls.Criteria criteria : sqlsCriteria) {
                Example.Criteria exampleCriteria = new Example.Criteria(this.propertyMap, this.exists, this.notNull);
                exampleCriteria.setAndOr(criteria.getAndOr());
                for (Sqls.Criterion criterion : criteria.getCriterions()) {
                    String condition = criterion.getCondition();
                    String andOr = criterion.getAndOr();
                    String property = criterion.getProperty();
                    Object[] values = criterion.getValues();
                    transformCriterion(exampleCriteria, condition, property, values, andOr);
                }
                exampleCriterias.add(exampleCriteria);
            }

            if (this.orderByClause.length() > 0) {
                this.orderByClause = new StringBuilder(this.orderByClause.substring(1, this.orderByClause.length()));
            }

            return new Example(this);
        }

        private void transformCriterion(Example.Criteria exampleCriteria, String condition, String property, Object[] values, String andOr) {
            if (values.length == 0) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(column(property) + " " + condition);
                } else {
                    exampleCriteria.addOrCriterion(column(property) + " " + condition);
                }
            } else if (values.length == 1) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(column(property) + " " + condition, values[0], property(property));
                } else {
                    exampleCriteria.addOrCriterion(column(property) + " " + condition, values[0], property(property));
                }
            } else if (values.length == 2) {
                if ("and".equals(andOr)) {
                    exampleCriteria.addCriterion(column(property) + " " + condition, values[0], values[1], property(property));
                } else {
                    exampleCriteria.addOrCriterion(column(property) + " " + condition, values[0], values[1], property(property));
                }
            }
        }

        private String column(String property) {
            if (propertyMap.containsKey(property)) {
                return propertyMap.get(property).getColumn();
            } else if (exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (propertyMap.containsKey(property)) {
                return property;
            } else if (exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String propertyforOderBy(String property) {
            if (StringUtil.isEmpty(property) || StringUtil.isEmpty(property.trim())) {
                throw new MapperException("接收的property为空！");
            }
            property = property.trim();
            if (!propertyMap.containsKey(property)) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            }
            return propertyMap.get(property).getColumn();
        }

        public Builder setDistinct(boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        public Builder setForUpdate(boolean forUpdate) {
            this.forUpdate = forUpdate;
            return this;
        }

        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }
    }

    public String getCountColumn() {
        return countColumn;
    }

    @Override
    public String getDynamicTableName() {
        return tableName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public Set<String> getSelectColumns() {
        if (selectColumns != null && selectColumns.size() > 0) {
            //不需要处理
        } else if (excludeColumns != null && excludeColumns.size() > 0) {
            Collection<EntityColumn> entityColumns = propertyMap.values();
            selectColumns = new LinkedHashSet<String>(entityColumns.size() - excludeColumns.size());
            for (EntityColumn column : entityColumns) {
                if (!excludeColumns.contains(column.getColumn())) {
                    selectColumns.add(column.getColumn());
                }
            }
        }
        return selectColumns;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    /**
     * 指定 count(property) 查询属性
     *
     * @param property
     */
    public void setCountProperty(String property) {
        if (propertyMap.containsKey(property)) {
            this.countColumn = propertyMap.get(property).getColumn();
        }
    }

    /**
     * 设置表名
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}