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
import tk.mybatis.mapper.entity.RowNumberExample;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

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
        if (parameter != null) {
            try {
                Set<EntityColumn> columns = EntityHelper.getColumns(parameter.getClass());
                Set<String> fieldSet = new HashSet<String>(Arrays.asList(fields.split(",")));
                for (EntityColumn column : columns) {
                    if (fieldSet.contains(column.getProperty())) {
                        Object value = column.getEntityField().getValue(parameter);
                        if (value != null) {
                            return true;
                        }
                    }
                }
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
        if (parameter != null) {
            try {
                if (parameter instanceof Example) {
                    List<Example.Criteria> criteriaList = ((Example) parameter).getOredCriteria();
                    if (criteriaList != null && criteriaList.size() > 0) {
                        return true;
                    }
                } else {
                    Method getter = parameter.getClass().getDeclaredMethod("getOredCriteria");
                    Object list = getter.invoke(parameter);
                    if (list != null && list instanceof List && ((List) list).size() > 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                throw new MapperException(SAFE_DELETE_ERROR, e);
            }
        }
        throw new MapperException(SAFE_DELETE_EXCEPTION);
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

    /**
     * 组建 ROW_NUMBER 函数的实际调用语句
     * @throws IllegalArgumentException 不存在 ROW_NUMBER 函数参数时抛出
     */
    public static String rowNumberSql(Object arg) {
        Example example = rowNumberExample(arg);
        checkRowNUmberParam(example);
        RowNumberExample param = example.getRowNumberParam();
        Class<?> clazz = example.getEntityClass();
        StringBuilder sb = new StringBuilder();
        List<String> partCols = convertToDbCols(param.getPartCols(), clazz);
        List<String> sortCols = convertToDbCols(param.getSortCols(), clazz);
        sb.append("ROW_NUMBER() OVER(PARTITION BY ")
                .append(join(partCols)).append(" ORDER BY ")
                .append(join(sortCols))
                .append(param.isSortOriented() ? " ASC" : " DESC")
                .append(") ");
        return sb.toString();
    }

    private static List<String> convertToDbCols(List<String> fields, Class<?> clazz) {
        if (fields == null || fields.isEmpty()) {
            return new ArrayList<String>();
        }
        Set<EntityColumn> cols = EntityHelper.getColumns(clazz);
        if (cols == null || cols.isEmpty()) {
            return new ArrayList<String>();
        }
        List<String> ans = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        for (EntityColumn col : cols) {
            map.put(col.getProperty(), col.getColumn());
        }
        for (String field : fields) {
            ans.add(map.get(field));
        }
        return ans;
    }

    private static String join(List<String> ss) {
        if (ss == null || ss.isEmpty()) return "";
        if (ss.size() == 1) return ss.get(0);
        int n = ss.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ss.get(i));
            if (i != n - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // 过滤条件，需要转换成对应的字符串形式
    public static String rowNumberCondition(Object arg) {
        Example example = rowNumberExample(arg);
        checkRowNUmberParam(example);
        RowNumberExample param = example.getRowNumberParam();
        return String.valueOf(param.getRank());
    }

    private static Example rowNumberExample(Object arg) {
        if (!(arg instanceof Example)) {
            throw new IllegalArgumentException("查询时参数类型不一致，应为 " + Example.class);
        }
        return (Example) arg;
    }

    private static void checkRowNUmberParam(Example example) {
        if (example == null || example.getRowNumberParam() == null) {
            throw new IllegalArgumentException("生成 ROW_NUMBER 查询参数时参数对象不能为 null");
        }
    }
}
