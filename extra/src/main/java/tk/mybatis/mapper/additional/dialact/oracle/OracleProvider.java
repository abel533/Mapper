package tk.mybatis.mapper.additional.dialact.oracle;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * @description: Oracle实现类
 * @author: qrqhuangcy
 * @date: 2018-11-15
 **/
public class OracleProvider extends MapperTemplate {

    public OracleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * <bind name="listNotEmptyCheck" value="@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, 'tk.mybatis.mapper.additional.dialect.oracle.DemoCountryMapper.insertList 方法参数为空')"/>
     * INSERT ALL
     * <foreach collection="list" item="record">
     *     INTO demo_country
     *     <trim prefix="(" suffix=")" suffixOverrides=",">country_id,country_name,country_code,</trim>
     *     VALUES
     *     <trim prefix="(" suffix=")" suffixOverrides=",">
     *         <bind name="country_idGenIdBind"  value="@tk.mybatis.mapper.genid.GenIdUtil@genId(record, 'countryId', @tk.mybatis.mapper.additional.insertlist.UUIdGenId@class, 'demo_country', 'country_id')"/>
     *         #{record.countryId},#{record.countryName},#{record.countryCode},
     *     </trim>
     * </foreach>
     * SELECT 1 FROM DUAL
     * 
     * @param ms
     * @return
     */
    public String insertList(MappedStatement ms){
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>\n");

        sql.append("INSERT ALL\n");
        sql.append("<foreach collection=\"list\" item=\"record\">\n");

        String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
        String columns = SqlHelper.insertColumns(entityClass, false, false, false);
        sql.append(" INTO ").append(tableName).append(" ").append(columns).append("\n");
        sql.append(" VALUES ");

        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");

        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //单独增加对 genId 方式的支持
        for (EntityColumn column : columnList) {
            if(column.getGenIdClass() != null){
                sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("record").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                sql.append(", '").append(tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }
        }
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>\n");

        sql.append("</foreach>\n");
        sql.append("SELECT 1 FROM DUAL");

        //System.out.println("sql mapper: \n" + sql.toString());
        return sql.toString();
    }
}
