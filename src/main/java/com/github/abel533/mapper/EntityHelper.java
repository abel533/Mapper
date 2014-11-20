package com.github.abel533.mapper;

import com.github.abel533.model.Country;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 初始化 Entity 结构
 *
 * @author huangyong
 * @since 1.0
 */
public class EntityHelper {

    public static class EntityColumn {
        private String property;
        private String column;
        private Class<?> javaType;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public Class<?> getJavaType() {
            return javaType;
        }

        public void setJavaType(Class<?> javaType) {
            this.javaType = javaType;
        }
    }

    /**
     * 实体类 => 表名
     */
    private static final Map<Class<?>, String> entityClassTableName = new HashMap<Class<?>, String>();

    /**
     * 实体类 => 全部列属性
     */
    private static final Map<Class<?>, List<EntityColumn>> entityClassColumns = new HashMap<Class<?>, List<EntityColumn>>();

    /**
     * 实体类 => 主键信息
     */
    private static final Map<Class<?>, List<EntityColumn>> entityClassPKColumns = new HashMap<Class<?>, List<EntityColumn>>();

    /**
     * 获取表名
     *
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        String tableName = entityClassTableName.get(entityClass);
        if (tableName == null) {
            initEntityNameMap(entityClass);
        }
        tableName = entityClassTableName.get(entityClass);
        if (tableName == null) {
            throw new RuntimeException("");
        }
        return tableName;
    }

    /**
     * 获取全部列
     *
     * @param entityClass
     * @return
     */
    public static List<EntityColumn> getColumns(Class<?> entityClass) {
        //可以起到初始化的作用
        getTableName(entityClass);
        return entityClassColumns.get(entityClass);
    }

    /**
     * 获取主键信息
     *
     * @param entityClass
     * @return
     */
    public static List<EntityColumn> getPKColumns(Class<?> entityClass) {
        //可以起到初始化的作用
        getTableName(entityClass);
        return entityClassPKColumns.get(entityClass);
    }

    /**
     * 获取查询的Select
     *
     * @param entityClass
     * @return
     */
    public static String getSelectColumns(Class<?> entityClass) {
        List<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn()).append(" ").append(entityColumn.getProperty().toUpperCase()).append(",");
        }
        return selectBuilder.substring(0, selectBuilder.length() - 1);
    }

    /**
     * 获取主键的Where语句
     *
     * @param entityClass
     * @return
     */
    public static String getPrimaryKeyWhere(Class<?> entityClass){
        List<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        StringBuilder whereBuilder = new StringBuilder();
        for (EntityHelper.EntityColumn column : entityColumns) {
            whereBuilder.append(column.getColumn()).append(" = ?").append(" and ");
        }
        return whereBuilder.substring(0,whereBuilder.length() - 4);
    }

    /**
     * 初始化实体属性
     *
     * @param entityClass
     */
    public static synchronized void initEntityNameMap(Class<?> entityClass) {
        //表名
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            entityClassTableName.put(entityClass, table.name());
        } else {
            entityClassTableName.put(entityClass, camelhumpToUnderline(entityClass.getSimpleName()).toUpperCase());
        }
        //TODO 为了准确，应该通过表名获取表对应的所有列
        /*MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
        while (metaObject.hasGetter("h")) {
            metaObject = SystemMetaObject.forObject(metaObject.getValue("h"));
        }
        if (metaObject.getValue("target") instanceof CachingExecutor) {
            CachingExecutor executor = (CachingExecutor)(metaObject.getValue("target"));
            Connection connection = executor.getTransaction().getConnection();
            System.out.println(connection!=null?connection.isClosed():"null");
            DatabaseMetaData data = connection.getMetaData();

            ResultSet colRet = data.getColumns(connection.getCatalog(),connection.getSchema(),"COUNTRY",null);
            String columnName;
            String columnType;
            while(colRet.next()) {
                columnName = colRet.getString("COLUMN_NAME");
                columnType = colRet.getString("TYPE_NAME");
                int datasize = colRet.getInt("COLUMN_SIZE");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int nullable = colRet.getInt("NULLABLE");
                System.out.println(columnName+" "+columnType+" "+datasize+" "+digits+" "+ nullable);
            }
        }*/
        //列
        List<Field> fieldList = getAllField(entityClass, null);
        List<EntityColumn> columnList = new ArrayList<EntityColumn>();
        List<EntityColumn> pkColumnList = new ArrayList<EntityColumn>();
        for (Field field : fieldList) {
            //排除字段
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            EntityColumn entityColumn = new EntityColumn();
            boolean isId = false;
            if (field.isAnnotationPresent(Id.class)) {
                isId = true;
            }
            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnName = column.name();
            } else {
                columnName = camelhumpToUnderline(field.getName());
            }
            entityColumn.setProperty(field.getName());
            entityColumn.setColumn(columnName.toUpperCase());
            entityColumn.setJavaType(field.getType());
            columnList.add(entityColumn);
            if (isId) {
                pkColumnList.add(entityColumn);
            }
        }
        if (pkColumnList.size() == 0) {
            pkColumnList = columnList;
        }
        entityClassColumns.put(entityClass, columnList);
        entityClassPKColumns.put(entityClass, pkColumnList);
    }

    /**
     * 将驼峰风格替换为下划线风格
     */
    public static String camelhumpToUnderline(String str) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
        }
        if (builder.charAt(0) == '_') {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 将下划线风格替换为驼峰风格
     */
    public static String underlineToCamelhump(String str) {
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }
        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }

    /**
     * 获取全部的Field
     *
     * @param entityClass
     * @param fieldList
     * @return
     */
    private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList) {
        if (fieldList == null) {
            fieldList = new ArrayList<Field>();
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));
        if (entityClass.getSuperclass() != null && !entityClass.getSuperclass().equals(Object.class)) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }

    public static void main(String[] args) {
        List<Field> fieldList = getAllField(Country.class, null);
        for (Field field : fieldList) {
            System.out.println(field.getName());
        }
    }

}
