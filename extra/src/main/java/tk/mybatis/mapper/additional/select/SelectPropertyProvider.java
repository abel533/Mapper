package tk.mybatis.mapper.additional.select;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Objects;

/**
 * @author jingkaihui
 * @date 2019/10/11
 */
public class SelectPropertyProvider extends MapperTemplate {

    private static final Log log = LogFactory.getLog(SelectPropertyProvider.class);

    public SelectPropertyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * Ba
     * 根据属性查询，只能有一个返回值，有多个结果时抛出异常，查询条件使用等号
     *
     * @param ms
     * @return
     */
    public String selectOneByProperty(MappedStatement ms) {
        String propertyHelper = SelectPropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        sql.append("<if test=\"@");
        sql.append(propertyHelper);
        sql.append("@existsWhereCondition(value, ");
        sql.append(isNotEmpty());
        sql.append(")");
        sql.append("\">\n");
        String entityClassName = entityClass.getName();
        //通过实体类名获取运行时属性对应的字段
        String ognl = new StringBuilder("${@")
                .append(propertyHelper)
                .append("@getColumnByProperty(@java.lang.Class@forName(\"")
                .append(entityClassName)
                .append("\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))}").toString();
        sql.append(ognl + " = #{value}\n");
        sql.append("</if>\n");
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性查询，查询条件使用等号
     *
     * @param ms
     * @return
     */
    public String selectByProperty(MappedStatement ms) {
        String propertyHelper = SelectPropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        sql.append("<if test=\"@");
        sql.append(propertyHelper);
        sql.append("@existsWhereCondition(value, ");
        sql.append(isNotEmpty());
        sql.append(")");
        sql.append("\">\n");
        String entityClassName = entityClass.getName();
        //通过实体类名获取运行时属性对应的字段
        String ognl = new StringBuilder("${@")
                .append(propertyHelper)
                .append("@getColumnByProperty(@java.lang.Class@forName(\"")
                .append(entityClassName)
                .append("\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))}").toString();
        sql.append(ognl + " = #{value}\n");
        sql.append("</if>\n");
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性查询，查询条件使用 in
     *
     * @param ms
     * @return
     */
    public String selectInByProperty(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        String entityClassName = entityClass.getName();
        String propertyHelper = SelectPropertyProvider.class.getName();
        String sqlSegment =
                "${@" + propertyHelper + "@getColumnByProperty(@java.lang.Class@forName(\"" + entityClassName + "\"),"
                        + "@tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))} in"
                        + "<foreach open=\"(\" close=\")\" separator=\",\" collection=\"values\" item=\"obj\">\n"
                        + "#{obj}\n"
                        + "</foreach>\n";
        sql.append(sqlSegment);
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性查询，查询条件使用 between
     *
     * @param ms
     * @return
     */
    public String selectBetweenByProperty(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        String entityClassName = entityClass.getName();
        String propertyHelper = SelectPropertyProvider.class.getName();
        String sqlSegment =
                "${@" + propertyHelper + "@getColumnByProperty(@java.lang.Class@forName(\"" + entityClassName + "\"),"
                        + "@tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))} "
                        + "between #{begin} and #{end}";
        sql.append(sqlSegment);
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性查询总数，查询条件使用等号
     *
     * @param ms
     * @return
     */
    public String existsWithProperty(MappedStatement ms) {
        String propertyHelper = SelectPropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCountExists(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        sql.append("<if test=\"@");
        sql.append(propertyHelper);
        sql.append("@existsWhereCondition(value, ");
        sql.append(isNotEmpty());
        sql.append(")");
        sql.append("\">\n");
        String entityClassName = entityClass.getName();
        //通过实体类名获取运行时属性对应的字段
        String ognl = new StringBuilder("${@")
                .append(propertyHelper)
                .append("@getColumnByProperty(@java.lang.Class@forName(\"")
                .append(entityClassName)
                .append("\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))}").toString();
        sql.append(ognl + " = #{value}\n");
        sql.append("</if>\n");
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性查询总数，查询条件使用等号
     *
     * @param ms
     * @return
     */
    public String selectCountByProperty(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        String propertyHelper = SelectPropertyProvider.class.getName();

        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>\n");
        sql.append("<if test=\"@");
        sql.append(propertyHelper);
        sql.append("@existsWhereCondition(value, ");
        sql.append(isNotEmpty());
        sql.append(")");
        sql.append("\">\n");
        String entityClassName = entityClass.getName();
        //通过实体类名获取运行时属性对应的字段
        String ognl = new StringBuilder("${@")
                .append(propertyHelper)
                .append("@getColumnByProperty(@java.lang.Class@forName(\"")
                .append(entityClassName)
                .append("\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))}").toString();
        sql.append(ognl + " = #{value}\n");
        sql.append("</if>\n");
        // 逻辑删除的未删除查询条件
        sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据实体Class和属性名获取对应的表字段名
     *
     * @param entityClass 实体Class对象
     * @param property    属性名
     * @return
     */
    public static String getColumnByProperty(Class<?> entityClass, String property) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        EntityColumn entityColumn = entityTable.getPropertyMap().get(property);
        return entityColumn.getColumn();
    }

    /**
     * 判断是否需要拼接 where 条件
     *
     * @param value
     * @param notEmpty
     * @return
     */
    public static boolean existsWhereCondition(Object value, boolean notEmpty) {
        boolean appendWhereCondition = true;
        if (Objects.isNull(value)) {
            log.warn("value is null! this will case no conditions after where keyword");
        } else {
            if (String.class.equals(value.getClass()) && notEmpty && StringUtil.isEmpty(value.toString())) {
                // 如果 value 是 String 类型，则根据是否允许为空串做进一步校验来决定是否拼接 where 条件
                appendWhereCondition = false;
            }
        }
        return appendWhereCondition;
    }
}
