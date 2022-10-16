package tk.mybatis.mapper.additional.upsert;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

public class BatchUpsertProvider extends MapperTemplate {

    public BatchUpsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpsert(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\";\" >");
        sql.append("INSERT INTO ");
        sql.append(tableName(entityClass));
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        String primaryKeyColumn = null;
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columns) {
            if (column.isId()) {
                primaryKeyColumn = column.getColumn();
            }
            if (column.isInsertable()) {
                sql.append(column.getColumn() + ",");
            }
        }
        sql.append("</trim>");
        sql.append(" VALUES ");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columns) {
            if (column.getGenIdClass() != null) {
                sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("record").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                sql.append(", '").append(tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }
        }
        for (EntityColumn column : columns) {
            if (!column.isInsertable()) {
                continue;
            }
            if (logicDeleteColumn != null && logicDeleteColumn == column) {
                sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                continue;
            }
            sql.append(column.getColumnHolder("record") + ",");
        }
        sql.append("</trim>");
        sql.append(" ON CONFLICT (" + primaryKeyColumn + ") DO UPDATE ");
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
        sql.append("</foreach>");
        return sql.toString();
    }
}
