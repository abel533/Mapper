package tk.mybatis.mapper.mapperhelper.resolve;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.FieldHelper;
import tk.mybatis.mapper.util.SimpleTypeUtil;
import tk.mybatis.mapper.util.SqlReservedWords;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author liuzh
 */
public class DefaultEntityResolve implements EntityResolve {

    @Override
    public EntityTable resolveEntity(Class<?> entityClass, Config config) {
        Style style = config.getStyle();
        //style，该注解优先于全局配置
        if (entityClass.isAnnotationPresent(NameStyle.class)) {
            NameStyle nameStyle = entityClass.getAnnotation(NameStyle.class);
            style = nameStyle.value();
        }

        //创建并缓存EntityTable
        EntityTable entityTable = null;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!"".equals(table.name())) {
                entityTable = new EntityTable(entityClass);
                entityTable.setTable(table);
            }
        }
        if (entityTable == null) {
            entityTable = new EntityTable(entityClass);
            //可以通过stye控制
            entityTable.setName(StringUtil.convertByStyle(entityClass.getSimpleName(), style));
        }
        entityTable.setEntityClassColumns(new LinkedHashSet<EntityColumn>());
        entityTable.setEntityClassPKColumns(new LinkedHashSet<EntityColumn>());
        //处理所有列
        List<EntityField> fields = null;
        if (config.isEnableMethodAnnotation()) {
            fields = FieldHelper.getAll(entityClass);
        } else {
            fields = FieldHelper.getFields(entityClass);
        }
        for (EntityField field : fields) {
            //如果启用了简单类型，就做简单类型校验，如果不是简单类型，直接跳过
            //3.5.0 如果启用了枚举作为简单类型，就不会自动忽略枚举类型
            //4.0 如果标记了 Column 或 ColumnType 注解，也不忽略
            if (config.isUseSimpleType()
                    && !field.isAnnotationPresent(Column.class)
                    && !field.isAnnotationPresent(ColumnType.class)
                    && !(SimpleTypeUtil.isSimpleType(field.getJavaType())
                            ||
                            (config.isEnumAsSimpleType() && Enum.class.isAssignableFrom(field.getJavaType())))) {
                continue;
            }
            processField(entityTable, field, config, style);
        }
        //当pk.size=0的时候使用所有列作为主键
        if (entityTable.getEntityClassPKColumns().size() == 0) {
            entityTable.setEntityClassPKColumns(entityTable.getEntityClassColumns());
        }
        entityTable.initPropertyMap();
        return entityTable;
    }

    /**
     * 处理字段
     *
     * @param entityTable
     * @param field
     * @param config
     * @param style
     */
    protected void processField(EntityTable entityTable, EntityField field, Config config, Style style) {
        //排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        //Id
        EntityColumn entityColumn = new EntityColumn(entityTable);
        //记录 field 信息，方便后续扩展使用
        entityColumn.setEntityField(field);
        if (field.isAnnotationPresent(Id.class)) {
            entityColumn.setId(true);
        }
        //Column
        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
            entityColumn.setUpdatable(column.updatable());
            entityColumn.setInsertable(column.insertable());
        }
        //ColumnType
        if (field.isAnnotationPresent(ColumnType.class)) {
            ColumnType columnType = field.getAnnotation(ColumnType.class);
            //是否为 blob 字段
            entityColumn.setBlob(columnType.isBlob());
            //column可以起到别名的作用
            if (StringUtil.isEmpty(columnName) && StringUtil.isNotEmpty(columnType.column())) {
                columnName = columnType.column();
            }
            if (columnType.jdbcType() != JdbcType.UNDEFINED) {
                entityColumn.setJdbcType(columnType.jdbcType());
            }
            if (columnType.typeHandler() != UnknownTypeHandler.class) {
                entityColumn.setTypeHandler(columnType.typeHandler());
            }
        }
        //列名
        if (StringUtil.isEmpty(columnName)) {
            columnName = StringUtil.convertByStyle(field.getName(), style);
        }
        //自动处理关键字
        if (StringUtil.isNotEmpty(config.getWrapKeyword()) && SqlReservedWords.containsWord(columnName)) {
            columnName = MessageFormat.format(config.getWrapKeyword(), columnName);
        }
        entityColumn.setProperty(field.getName());
        entityColumn.setColumn(columnName);
        entityColumn.setJavaType(field.getJavaType());
        //OrderBy
        processOrderBy(entityTable, field, entityColumn);
        //处理主键策略
        processKeyGenerator(entityTable, field, entityColumn);
        entityTable.getEntityClassColumns().add(entityColumn);
        if (entityColumn.isId()) {
            entityTable.getEntityClassPKColumns().add(entityColumn);
        }
    }

    /**
     * 处理排序
     *
     * @param entityTable
     * @param field
     * @param entityColumn
     */
    protected void processOrderBy(EntityTable entityTable, EntityField field, EntityColumn entityColumn){
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if ("".equals(orderBy.value())) {
                entityColumn.setOrderBy("ASC");
            } else {
                entityColumn.setOrderBy(orderBy.value());
            }
        }
    }

    /**
     * 处理主键策略
     *
     * @param entityTable
     * @param field
     * @param entityColumn
     */
    protected void processKeyGenerator(EntityTable entityTable, EntityField field, EntityColumn entityColumn){
        //KeySql 优先级最高
        if(field.isAnnotationPresent(KeySql.class)){
            processKeySql(entityTable, entityColumn, field.getAnnotation(KeySql.class));
        }
        else if (field.isAnnotationPresent(SequenceGenerator.class)) {
            //序列
            processSequenceGenerator(entityTable, entityColumn, field.getAnnotation(SequenceGenerator.class));
        }
        else if (field.isAnnotationPresent(GeneratedValue.class)) {
            //执行 sql - selectKey
            processGeneratedValue(entityTable, entityColumn, field.getAnnotation(GeneratedValue.class));
        }
    }

    /**
     * 处理 SequenceGenerator 注解
     *
     * @param entityTable
     * @param entityColumn
     * @param sequenceGenerator
     */
    protected void processSequenceGenerator(EntityTable entityTable, EntityColumn entityColumn, SequenceGenerator sequenceGenerator){
        if ("".equals(sequenceGenerator.sequenceName())) {
            throw new MapperException(entityTable.getEntityClass() + "字段" + entityColumn.getProperty() + "的注解@SequenceGenerator未指定sequenceName!");
        }
        entityColumn.setSequenceName(sequenceGenerator.sequenceName());
    }

    /**
     * 处理 GeneratedValue 注解
     *
     * @param entityTable
     * @param entityColumn
     * @param generatedValue
     */
    protected void processGeneratedValue(EntityTable entityTable, EntityColumn entityColumn, GeneratedValue generatedValue){
        if ("UUID".equals(generatedValue.generator())) {
            entityColumn.setUuid(true);
        } else if ("JDBC".equals(generatedValue.generator())) {
            entityColumn.setIdentity(true);
            entityColumn.setGenerator("JDBC");
            entityTable.setKeyProperties(entityColumn.getProperty());
            entityTable.setKeyColumns(entityColumn.getColumn());
        } else {
            //允许通过generator来设置获取id的sql,例如mysql=CALL IDENTITY(),hsqldb=SELECT SCOPE_IDENTITY()
            //允许通过拦截器参数设置公共的generator
            if (generatedValue.strategy() == GenerationType.IDENTITY) {
                //mysql的自动增长
                entityColumn.setIdentity(true);
                if (!"".equals(generatedValue.generator())) {
                    String generator = null;
                    IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(generatedValue.generator());
                    if (identityDialect != null) {
                        generator = identityDialect.getIdentityRetrievalStatement();
                    } else {
                        generator = generatedValue.generator();
                    }
                    entityColumn.setGenerator(generator);
                }
            } else {
                throw new MapperException(entityColumn.getProperty()
                        + " - 该字段@GeneratedValue配置只允许以下几种形式:" +
                        "\n1.全部数据库通用的@GeneratedValue(generator=\"UUID\")" +
                        "\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"JDBC\\\")  " +
                        "\n3.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"Mysql\"])");
            }
        }
    }

    /**
     * 处理 KeySql 注解
     *
     * @param entityTable
     * @param entityColumn
     * @param keySql
     */
    protected void processKeySql(EntityTable entityTable, EntityColumn entityColumn, KeySql keySql){
        if(keySql.useGeneratedKeys()){
            entityColumn.setIdentity(true);
            entityColumn.setGenerator("JDBC");
            entityTable.setKeyProperties(entityColumn.getProperty());
            entityTable.setKeyColumns(entityColumn.getColumn());
        } else if(keySql.dialect() != IdentityDialect.DEFAULT){
            //自动增长
            entityColumn.setIdentity(true);
            entityColumn.setOrder(ORDER.AFTER);
            entityColumn.setGenerator(keySql.dialect().getIdentityRetrievalStatement());
        } else {
            if(StringUtil.isEmpty(keySql.sql())){
                throw new MapperException(entityTable.getEntityClass().getCanonicalName()
                        + " 类中的 @KeySql 注解配置无效!");
            }
            entityColumn.setIdentity(true);
            entityColumn.setOrder(keySql.order());
            entityColumn.setGenerator(keySql.sql());
        }
    }

}
