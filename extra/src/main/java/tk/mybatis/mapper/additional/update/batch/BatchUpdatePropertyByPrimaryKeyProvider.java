package tk.mybatis.mapper.additional.update.batch;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Set;

public class BatchUpdatePropertyByPrimaryKeyProvider extends MapperTemplate {

    private static final Log log = LogFactory.getLog(BatchUpdatePropertyByPrimaryKeyProvider.class);

    public BatchUpdatePropertyByPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpdateFieldByIdList(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        // set
        appendSet(sql, entityClass);
        // where
        appendWhereIdList(sql, entityClass, true);
        return sql.toString();
    }

    public String batchUpdateFieldListByIdList(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        // set
        appendFileListSet(sql, entityClass);
        // where
        appendWhereIdList(sql, entityClass, true);
        return sql.toString();
    }

    private void appendSet(StringBuilder sql, Class<?> entityClass) {
        sql.append("<set>");
        //通过实体类名获取运行时属性对应的字段
        String ognl = new StringBuilder("${@")
            .append(getProviderName())
            .append("@getColumnByProperty(@java.lang.Class@forName(\"")
            .append(entityClass.getName())
            .append("\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(fn))}").toString();
        sql.append(ognl + " = #{value}\n");
        sql.append("</set>");
    }

    private void appendWhereIdList(StringBuilder sql, Class<?> entityClass, boolean useVersion) {
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append("<bind name=\"notEmptyListCheck\" value=\"@tk.mybatis.mapper.additional.update.batch.BatchUpdatePropertyByPrimaryKeyProviderr@notEmpty(");
            sql.append("idList, 'idList 不能为空')\"/>");
            sql.append("<where>");
            sql.append("<foreach collection=\"idList\" item=\"id\" separator=\",\" open=\"");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("(\" close=\")\">");
            sql.append("#{id}");
            sql.append("</foreach>");
            if (useVersion) {
                sql.append(SqlHelper.whereVersion(entityClass));
            }
            sql.append("</where>");
        } else {
            throw new MapperException("继承 ByIdList 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }

    private void appendFileListSet(StringBuilder sql, Class<?> entityClass) {
        sql.append("<set>");
        sql.append("<foreach collection=\"fieldList\" item=\"field\" separator=\",\" >");
        //通过实体类名获取运行时属性对应的字段
        String ognl = "${@" +
            getProviderName() +
            "@getColumnByProperty(@java.lang.Class@forName(\"" +
            entityClass.getName() +
            "\"), @tk.mybatis.mapper.weekend.reflection.Reflections@fnToFieldName(field.fn))}";
        sql.append(ognl + " = #{field.value}\n");
        sql.append("</foreach>");
        sql.append("</set>");

    }

    private String getProviderName() {
        return BatchUpdatePropertyByPrimaryKeyProvider.class.getName();
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
     * 保证 idList 不能为空
     *
     * @param list
     * @param errorMsg
     */
    public static void notEmpty(List<?> list, String errorMsg){
        if(list == null || list.size() == 0){
            throw new MapperException(errorMsg);
        }
    }


}
