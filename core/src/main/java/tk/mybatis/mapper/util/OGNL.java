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

package tk.mybatis.mapper.util;

import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * OGNL静态方法
 *
 * @author liuzh
 */
public abstract class OGNL {
    public static final String SAFE_DELETE_ERROR = "通用 Mapper 安全检查: 对查询条件参数进行检查时出错!";
    public static final String SAFE_DELETE_EXCEPTION = "通用 Mapper 安全检查: 当前操作的方法没有指定查询条件，不允许执行该操作!";

    /**
     * 校验通用 Example 的 entityClass 和当前方法是否匹配
     *
     * @param parameter
     * @param entityFullName
     * @return
     */
    public static boolean checkExampleEntityClass(Object parameter, String entityFullName) {
        if (parameter != null && parameter instanceof Example && StringUtil.isNotEmpty(entityFullName)) {
            Example example = (Example) parameter;
            Class<?> entityClass = example.getEntityClass();
            if (!entityClass.getName().equals(entityFullName)) {
                throw new MapperException("当前 Example 方法对应实体为:" + entityFullName
                        + ", 但是参数 Example 中的 entityClass 为:" + entityClass.getName());
            }
        }
        return true;
    }

    /**
     * 检查 parameter 对象中指定的 fields 是否全是 null，如果是则抛出异常
     *
     * @param parameter
     * @param fields
     * @return
     */
    public static boolean notAllNullParameterCheck(Object parameter, String fields) {
        return notAllNullParameterCheck(parameter, fields, false);
    }

    /**
     * 检查 parameter 对象中指定的 fields 是否全是 null 或空字符串，如果是则抛出异常
     *
     * @param parameter
     * @param fields
     * @param notEmpty
     * @return
     */
    public static boolean notAllNullParameterCheck(Object parameter, String fields, boolean notEmpty) {
        if (parameter != null) {
            try {
                Set<EntityColumn> columns = EntityHelper.getColumns(parameter.getClass());
                Set<String> fieldSet = new HashSet<String>(Arrays.asList(fields.split(",")));
                for (EntityColumn column : columns) {
                    if (fieldSet.contains(column.getProperty()) && !isLogicDeleteColumn(column)) {
                        Object value = column.getEntityField().getValue(parameter);
                        if (isEffectiveValue(value, notEmpty)) {
                            return true;
                        }
                    }
                }
            } catch (MapperException e) {
                throw e;
            } catch (Exception e) {
                throw new MapperException(SAFE_DELETE_ERROR, e);
            }
        }
        throw new MapperException(SAFE_DELETE_EXCEPTION);
    }

    /**
     * 校验集合类型参数不能为空
     *
     * @param parameter
     * @param error
     * @return
     */
    public static boolean notEmptyCollectionCheck(Object parameter, String error) {
        if (parameter == null || (parameter instanceof Collection && ((Collection) parameter).size() == 0)) {
            throw new IllegalArgumentException(error);
        }
        return true;
    }

    /**
     * 检查 parameter 对象中指定的 fields 是否全是 null，如果是则抛出异常
     *
     * @param parameter
     * @return
     */
    public static boolean exampleHasAtLeastOneCriteriaCheck(Object parameter) {
        return exampleHasAtLeastOneCriteriaCheck(parameter, true);
    }

    public static boolean exampleHasAtLeastOneCriteriaCheck(Object parameter, boolean notEmpty) {
        if (parameter != null) {
            try {
                List<?> criteriaList = getOredCriteria(parameter);
                if (criteriaList != null && criteriaList.size() > 0) {
                    boolean hasCriterion = false;
                    for (Object criteria : criteriaList) {
                        if (criteriaHasCriterion(criteria, notEmpty)) {
                            hasCriterion = true;
                        }
                    }
                    if (hasCriterion) {
                        return true;
                    }
                }
            } catch (MapperException e) {
                throw e;
            } catch (Exception e) {
                throw new MapperException(SAFE_DELETE_ERROR, e);
            }
        }
        throw new MapperException(SAFE_DELETE_EXCEPTION);
    }

    private static List<?> getOredCriteria(Object parameter) throws Exception {
        if (parameter instanceof Example) {
            return ((Example) parameter).getOredCriteria();
        }
        Method getter = parameter.getClass().getMethod("getOredCriteria");
        if (!getter.isAccessible()) {
            getter.setAccessible(true);
        }
        Object list = getter.invoke(parameter);
        if (list instanceof List) {
            return (List<?>) list;
        }
        return null;
    }

    private static boolean criteriaHasCriterion(Object criteria, boolean notEmpty) throws Exception {
        if (criteria == null) {
            return false;
        }
        List<?> criterionList = getCriteriaList(criteria);
        if (criterionList != null) {
            boolean hasCriterion = false;
            for (Object criterion : criterionList) {
                if (criterion != null) {
                    emptyCriterionValueCheck(criterion, notEmpty);
                    hasCriterion = true;
                }
            }
            return hasCriterion;
        }
        Boolean valid = invokeBoolean(criteria, "isValid");
        return valid != null && valid;
    }

    private static List<?> getCriteriaList(Object criteria) throws Exception {
        if (criteria instanceof Example.Criteria) {
            return ((Example.Criteria) criteria).getAllCriteria();
        }
        Object list = invoke(criteria, "getAllCriteria");
        if (list instanceof List) {
            return (List<?>) list;
        }
        list = invoke(criteria, "getCriteria");
        if (list instanceof List) {
            return (List<?>) list;
        }
        return null;
    }

    private static void emptyCriterionValueCheck(Object criterion, boolean notEmpty) throws Exception {
        Boolean noValue = invokeBoolean(criterion, "isNoValue");
        if (Boolean.TRUE.equals(noValue)) {
            return;
        }
        Boolean singleValue = invokeBoolean(criterion, "isSingleValue");
        if (Boolean.TRUE.equals(singleValue) && isEmptyConditionValue(invoke(criterion, "getValue"), notEmpty)) {
            throw new MapperException(SAFE_DELETE_EXCEPTION);
        }
        Boolean betweenValue = invokeBoolean(criterion, "isBetweenValue");
        if (Boolean.TRUE.equals(betweenValue)
                && (isEmptyConditionValue(invoke(criterion, "getValue"), notEmpty)
                || isEmptyConditionValue(invoke(criterion, "getSecondValue"), notEmpty))) {
            throw new MapperException(SAFE_DELETE_EXCEPTION);
        }
        Boolean listValue = invokeBoolean(criterion, "isListValue");
        if (Boolean.TRUE.equals(listValue) && isEmptyConditionValue(invoke(criterion, "getValue"), notEmpty)) {
            throw new MapperException(SAFE_DELETE_EXCEPTION);
        }
    }

    private static Boolean invokeBoolean(Object target, String methodName) throws Exception {
        Object value = invoke(target, methodName);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    private static Object invoke(Object target, String methodName) throws Exception {
        try {
            Method method = target.getClass().getMethod(methodName);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(target);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static boolean isLogicDeleteColumn(EntityColumn column) {
        return column.getEntityField().isAnnotationPresent(LogicDelete.class);
    }

    private static boolean isEffectiveValue(Object value, boolean notEmpty) {
        return !isEmptyConditionValue(value, notEmpty);
    }

    private static boolean isEmptyConditionValue(Object value, boolean notEmpty) {
        if (value == null) {
            return true;
        }
        if (notEmpty && value instanceof String && StringUtil.isEmpty((String) value)) {
            return true;
        }
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
            return true;
        }
        if (value.getClass().isArray() && Array.getLength(value) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含自定义查询列
     *
     * @param parameter
     * @return
     */
    public static boolean hasSelectColumns(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            if (example.getSelectColumns() != null && example.getSelectColumns().size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含自定义 Count 列
     *
     * @param parameter
     * @return
     */
    public static boolean hasCountColumn(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            return StringUtil.isNotEmpty(example.getCountColumn());
        }
        return false;
    }

    /**
     * 是否包含 forUpdate
     *
     * @param parameter
     * @return
     */
    public static boolean hasForUpdate(Object parameter) {
        if (parameter != null && parameter instanceof Example) {
            Example example = (Example) parameter;
            return example.isForUpdate();
        }
        return false;
    }

    /**
     * 不包含自定义查询列
     *
     * @param parameter
     * @return
     */
    public static boolean hasNoSelectColumns(Object parameter) {
        return !hasSelectColumns(parameter);
    }

    /**
     * 判断参数是否支持动态表名
     *
     * @param parameter
     * @return true支持，false不支持
     */
    public static boolean isDynamicParameter(Object parameter) {
        if (parameter != null && parameter instanceof IDynamicTableName) {
            return true;
        }
        return false;
    }

    /**
     * 判断参数是否b支持动态表名
     *
     * @param parameter
     * @return true不支持，false支持
     */
    public static boolean isNotDynamicParameter(Object parameter) {
        return !isDynamicParameter(parameter);
    }

    /**
     * 判断条件是 and 还是 or
     *
     * @param parameter
     * @return
     */
    public static String andOr(Object parameter) {
        if (parameter instanceof Example.Criteria) {
            return ((Example.Criteria) parameter).getAndOr();
        } else if (parameter instanceof Example.Criterion) {
            return ((Example.Criterion) parameter).getAndOr();
        } else if (parameter.getClass().getName().endsWith("Criteria")) {
            return "or";
        } else {
            return "and";
        }
    }

    /**
     * 拼接逻辑删除字段的未删除查询条件
     *
     * @param parameter
     * @return
     */
    public static String andNotLogicDelete(Object parameter) {
        String result = "";
        if (parameter instanceof Example) {
            Example example = (Example) parameter;
            Map<String, EntityColumn> propertyMap = example.getPropertyMap();

            for (Map.Entry<String, EntityColumn> entry : propertyMap.entrySet()) {
                EntityColumn column = entry.getValue();
                if (column.getEntityField().isAnnotationPresent(LogicDelete.class)) {
                    // 未逻辑删除的条件
                    Integer logicDeletedValue = SqlHelper.getLogicDeletedValue(column, false);
                    if(logicDeletedValue==null){
                        result = column.getColumn() + " is null ";
                    }else {
                        result = column.getColumn() + " = " + logicDeletedValue;
                    }

                    // 如果Example中有条件，则拼接" and "，
                    // 如果是空的oredCriteria，则where中只有逻辑删除注解的未删除条件
                    if (hasWhereCause(example)) {
                        result += " and ";
                    }
                }
            }
        }
        return result;
    }

    /**
     * 检查是否存在where条件，存在返回true，不存在返回false.
     *
     * @param example
     * @return
     */
    private static boolean hasWhereCause(Example example) {
        if (example.getOredCriteria() == null || example.getOredCriteria().size() == 0) {
            return false;
        }
        for (Example.Criteria oredCriterion : example.getOredCriteria()) {
            if (oredCriterion.getAllCriteria().size() != 0) {
                return true;
            }
        }
        return false;
    }

}
