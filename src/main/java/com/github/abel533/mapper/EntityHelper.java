package com.github.abel533.mapper;

import com.github.abel533.model.Country;

import javax.persistence.*;
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
        private String sequenceName;
        private Boolean ID = Boolean.FALSE;
        private Boolean UUID = Boolean.FALSE;
        private Boolean IDENTITY = Boolean.FALSE;

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

        public String getSequenceName() {
            return sequenceName;
        }

        public void setSequenceName(String sequenceName) {
            this.sequenceName = sequenceName;
        }

        public Boolean getID() {
            return ID;
        }

        public void setID(Boolean ID) {
            this.ID = ID;
        }

        public Boolean getUUID() {
            return UUID;
        }

        public void setUUID(Boolean UUID) {
            this.UUID = UUID;
        }

        public Boolean getIDENTITY() {
            return IDENTITY;
        }

        public void setIDENTITY(Boolean IDENTITY) {
            this.IDENTITY = IDENTITY;
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
    public static String getPrimaryKeyWhere(Class<?> entityClass) {
        List<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        StringBuilder whereBuilder = new StringBuilder();
        for (EntityHelper.EntityColumn column : entityColumns) {
            whereBuilder.append(column.getColumn()).append(" = ?").append(" and ");
        }
        return whereBuilder.substring(0, whereBuilder.length() - 4);
    }

    /**
     * 初始化实体属性
     *
     * @param entityClass
     */
    public static synchronized void initEntityNameMap(Class<?> entityClass) {
        if (entityClassTableName.get(entityClass) != null) {
            return;
        }
        //表名
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            entityClassTableName.put(entityClass, table.name());
        } else {
            entityClassTableName.put(entityClass, camelhumpToUnderline(entityClass.getSimpleName()).toUpperCase());
        }
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
            if (field.isAnnotationPresent(Id.class)) {
                entityColumn.setID(Boolean.TRUE);
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
            //TODO 主键策略 - Oracle序列，MySql自动增长，UUID
            if (field.isAnnotationPresent(SequenceGenerator.class)) {
                SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
                entityColumn.setSequenceName(sequenceGenerator.sequenceName());
            }
            if (field.isAnnotationPresent(GeneratedValue.class)) {
                GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                if (generatedValue.generator().equals("UUID")) {
                    if (field.getType().equals(String.class)) {
                        entityColumn.setUUID(Boolean.TRUE);
                    } else {
                        throw new RuntimeException(field.getName() + " - 该字段@GeneratedValue配置为UUID，但该字段类型不是String");
                    }
                } else {
                    if (generatedValue.strategy() == GenerationType.IDENTITY) {
                        //mysql的自动增长
                        entityColumn.setIDENTITY(Boolean.TRUE);
                    } else {
                        throw new RuntimeException(field.getName()
                                + " - 该字段@GeneratedValue配置只允许两种形式，全部数据库通用的@GeneratedValue(generator=\"UUID\") 或者 " +
                                "mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY)");
                    }
                }
            }
            columnList.add(entityColumn);
            if (entityColumn.getID()) {
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
