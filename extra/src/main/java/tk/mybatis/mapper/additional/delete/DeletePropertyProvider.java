package tk.mybatis.mapper.additional.delete;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

/**
 * @author jingkaihui
 * @date 2020/3/30
 */
public class DeletePropertyProvider extends MapperTemplate {

    public DeletePropertyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据属性删除，条件使用等号
     *
     * @param ms
     * @return
     */
    public String deleteByProperty(MappedStatement ms) {
        String propertyHelper = DeletePropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        // 如果是逻辑删除，则修改为更新表，修改逻辑删除字段的值
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append("<where>\n");
        sql.append("<if test=\"false==");
        sql.append("@");
        sql.append(propertyHelper);
        sql.append("@isNull(value, ");
        sql.append(getConfig().isSafeDelete());
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
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据属性删除，条件使用等号
     *
     * @param ms
     * @return
     */
    public String deleteInByProperty(MappedStatement ms) {
        String propertyHelper = DeletePropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        // 如果是逻辑删除，则修改为更新表，修改逻辑删除字段的值
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append("<where>\n");
        String entityClassName = entityClass.getName();
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
     * 根据属性删除，删除条件使用 between
     *
     * @param ms
     * @return
     */
    public String deleteBetweenByProperty(MappedStatement ms) {
        String propertyHelper = DeletePropertyProvider.class.getName();
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        // 如果是逻辑删除，则修改为更新表，修改逻辑删除字段的值
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append("<where>\n");
        String entityClassName = entityClass.getName();
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

    public static boolean isNull(Object value, boolean safeDelete) {
        boolean isNull = false;
        if (safeDelete) {
            if (null == value) {
                throw new MapperException("安全删除模式下，不允许执行不带查询条件的 delete 方法");
            }
        } else {
            if (null == value) {
                isNull = true;
            }
        }
        return isNull;
    }
}
