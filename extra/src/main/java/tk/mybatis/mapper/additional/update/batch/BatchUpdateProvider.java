package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class BatchUpdateProvider extends MapperTemplate {

    public BatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpdate(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\";\" >");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, false));
        sql.append(SqlHelper.wherePKColumns(entityClass, "record", true));
        sql.append("</foreach>");
        return sql.toString();
    }

    public String batchUpdateSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\";\" >");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
        sql.append(SqlHelper.wherePKColumns(entityClass, "record", true));
        sql.append("</foreach>");
        return sql.toString();
    }
}
