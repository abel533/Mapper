/*
	The MIT License (MIT)

	Copyright (c) 2014 abel533@gmail.com

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

package com.github.abel533.mapper;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 实体类工具类
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
public class EntityHelper {

    public static class EntityColumn {
        private String property;
        private String column;
        private Class<?> javaType;
        private String sequenceName;
        private boolean id = false;
        private boolean uuid = false;
        private boolean identity = false;
        private String generator;

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

        public boolean isId() {
            return id;
        }

        public void setId(boolean id) {
            this.id = id;
        }

        public boolean isUuid() {
            return uuid;
        }

        public void setUuid(boolean uuid) {
            this.uuid = uuid;
        }

        public boolean isIdentity() {
            return identity;
        }

        public void setIdentity(boolean identity) {
            this.identity = identity;
        }

        public String getGenerator() {
            return generator;
        }

        public void setGenerator(String generator) {
            this.generator = generator;
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
            tableName = entityClassTableName.get(entityClass);
        }
        if (tableName == null) {
            throw new RuntimeException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
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
        boolean skipAlias = Map.class.isAssignableFrom(entityClass);
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn());
            if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                selectBuilder.append(" ").append(entityColumn.getProperty().toUpperCase()).append(",");
            } else {
                selectBuilder.append(",");
            }
        }
        return selectBuilder.substring(0, selectBuilder.length() - 1);
    }

    /**
     * 获取查询的Select
     *
     * @param entityClass
     * @return
     */
    public static String getAllColumns(Class<?> entityClass) {
        List<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn()).append(",");
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
            whereBuilder.append(column.getColumn()).append(" = ?").append(" AND ");
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
                entityColumn.setId(true);
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
            //主键策略 - Oracle序列，MySql自动增长，UUID
            if (field.isAnnotationPresent(SequenceGenerator.class)) {
                SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
                if (sequenceGenerator.sequenceName().equals("")) {
                    throw new RuntimeException(entityClass + "字段" + field.getName() + "的注解@SequenceGenerator未指定sequenceName!");
                }
                entityColumn.setSequenceName(sequenceGenerator.sequenceName());
            } else if (field.isAnnotationPresent(GeneratedValue.class)) {
                GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                if (generatedValue.generator().equals("UUID")) {
                    if (field.getType().equals(String.class)) {
                        entityColumn.setUuid(true);
                    } else {
                        throw new RuntimeException(field.getName() + " - 该字段@GeneratedValue配置为UUID，但该字段类型不是String");
                    }
                } else {
                    //允许通过generator来设置获取id的sql,例如mysql=CALL IDENTITY(),hsqldb=SELECT SCOPE_IDENTITY()
                    //允许通过拦截器参数设置公共的generator
                    if (generatedValue.strategy() == GenerationType.IDENTITY) {
                        //mysql的自动增长
                        entityColumn.setIdentity(true);
                        if (!generatedValue.generator().equals("")) {
                            entityColumn.setGenerator(generatedValue.generator());
                        }
                    } else {
                        throw new RuntimeException(field.getName()
                                + " - 该字段@GeneratedValue配置只允许两种形式，全部数据库通用的@GeneratedValue(generator=\"UUID\") 或者 " +
                                "类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"CALL IDENTITY()\"])");
                    }
                }
            }
            columnList.add(entityColumn);
            if (entityColumn.isId()) {
                pkColumnList.add(entityColumn);
            }
        }
        if (pkColumnList.size() == 0) {
            pkColumnList = columnList;
        }
        entityClassColumns.put(entityClass, columnList);
        entityClassPKColumns.put(entityClass, pkColumnList);
    }

    public static void main(String[] args) {
        System.out.println(camelhumpToUnderline("userName"));
        System.out.println(camelhumpToUnderline("userPassWord"));
        System.out.println(camelhumpToUnderline("ISO9001"));
        System.out.println(camelhumpToUnderline("hello_world"));
    }

    /**
     * 将驼峰风格替换为下划线风格
     */
    public static String camelhumpToUnderline(String str) {
        final int size;
        final char[] chars;
        final StringBuilder sb = new StringBuilder(
                (size = (chars = str.toCharArray()).length) * 3 / 2 + 1);
        char c;
        for (int i = 0; i < size; i++) {
            c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(c);
            } else {
                sb.append(toUpperAscii(c));
            }
        }
        return sb.charAt(0) == '_'? sb.substring(1): sb.toString();
    }

    public static boolean isUppercaseAlpha(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    public static char toUpperAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c -= (char) 0x20;
        }
        return c;
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
        for (Field field : fields) {
            //排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        if (entityClass.getSuperclass() != null
                && !entityClass.getSuperclass().equals(Object.class)
                && !Map.class.isAssignableFrom(entityClass.getSuperclass())
                && !Collection.class.isAssignableFrom(entityClass.getSuperclass())) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }
}