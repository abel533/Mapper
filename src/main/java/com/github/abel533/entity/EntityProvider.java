/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.abel533.entity;

import com.github.abel533.mapperhelper.EntityHelper;
import com.github.abel533.mapperhelper.MapperTemplate;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 * @author hanxue
 */
public class EntityProvider extends BaseProvider {

    /**
     * 查询，入参可以是Entity.class或new Entity()
     *
     * @param params
     * @return
     */
    public String select(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass;
            if (entity instanceof Class<?>) {
                entityClass = (Class<?>) entity;
                entity = null;
            } else {
                entityClass = getEntityClass(params);
            }
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            SELECT(EntityHelper.getAllColumns(entityClass));
            FROM(entityTable.getName());
            if (entity != null) {
                final MetaObject metaObject = MapperTemplate.forObject(entity);
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                    Object value = metaObject.getValue(column.getProperty());
                    if (value == null) {
                        continue;
                    } else if (column.getJavaType().equals(String.class)) {
                        if (isNotEmpty((String) value)) {
                            WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                        }
                    } else {
                        WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                    }
                }
            }
        }}.toString();
    }

    /**
     * 查询，入参可以是Entity.class或new Entity()
     *
     * @param params
     * @return
     */
    public String count(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass;
            if (entity instanceof Class<?>) {
                entityClass = (Class<?>) entity;
                entity = null;
            } else {
                entityClass = getEntityClass(params);
            }
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            SELECT("count(*)");
            FROM(entityTable.getName());
            if (entity != null) {
                MetaObject metaObject = MapperTemplate.forObject(entity);
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                    Object value = metaObject.getValue(column.getProperty());
                    if (value == null) {
                        continue;
                    } else if (column.getJavaType().equals(String.class)) {
                        if (isNotEmpty((String) value)) {
                            WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                        }
                    } else {
                        WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                    }
                }
            }
        }}.toString();
    }

    /**
     * 通过主键查询，主键字段都不能为空
     *
     * @param params
     * @return
     */
    public String selectByPrimaryKey(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            SELECT(EntityHelper.getAllColumns(entityClass));
            FROM(entityTable.getName());
            if (entityTable.getEntityClassPKColumns().size() == 1) {
                EntityHelper.EntityColumn column = entityTable.getEntityClassPKColumns().iterator().next();
                notNullKeyProperty(column.getProperty(), entity);
                WHERE(column.getColumn() + "=#{key}");
            } else {
                MetaObject metaObject = MapperTemplate.forObject(entity);
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassPKColumns()) {
                    Object value = metaObject.getValue(column.getProperty());
                    notNullKeyProperty(column.getProperty(), value);
                    WHERE(column.getColumn() + "=#{key." + column.getProperty() + "}");
                }
            }
        }}.toString();
    }

    /**
     * 新增
     *
     * @param params
     * @return
     */
    public String insert(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            INSERT_INTO(entityTable.getName());
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                //TODO 关于主键为序列和UUID的情况需要特殊处理
                VALUES(column.getColumn(), "#{record." + column.getProperty() + "}");
            }
        }}.toString();
    }

    public String insertSelective(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            INSERT_INTO(entityTable.getName());
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                //TODO 关于主键为序列和UUID的情况需要特殊处理
                //TODO 非主键字段如果是空，就不插入，这种情况会使用表的默认值
                VALUES(column.getColumn(), "#{record." + column.getProperty() + "}");
            }
        }}.toString();
    }

    /**
     * 通过查询条件删除
     *
     * @param params
     * @return
     */
    public String delete(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            DELETE_FROM(entityTable.getName());
            boolean hasValue = false;
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                Object value = metaObject.getValue(column.getProperty());
                if (value == null) {
                    continue;
                } else if (column.getJavaType().equals(String.class)) {
                    if (isNotEmpty((String) value)) {
                        WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                        hasValue = true;
                    }
                } else {
                    WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                    hasValue = true;
                }
            }
            if (!hasValue) {
                throw new UnsupportedOperationException("delete方法不支持删除全表的操作!");
            }
        }}.toString();
    }

    /**
     * 通过主键删除
     *
     * @param params
     * @return
     */
    public String deleteByPrimaryKey(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            DELETE_FROM(entityTable.getName());
            if (entityTable.getEntityClassPKColumns().size() == 1) {
                EntityHelper.EntityColumn column = entityTable.getEntityClassPKColumns().iterator().next();
                notNullKeyProperty(column.getProperty(), entity);
                WHERE(column.getColumn() + "=#{key}");
            } else {
                MetaObject metaObject = MapperTemplate.forObject(entity);
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassPKColumns()) {
                    Object value = metaObject.getValue(column.getProperty());
                    notNullKeyProperty(column.getProperty(), value);
                    WHERE(column.getColumn() + "=#{key." + column.getProperty() + "}");
                }
            }
        }}.toString();
    }

    /**
     * 通过主键更新
     *
     * @param params
     * @return
     */
    public String updateByPrimaryKey(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            UPDATE(entityTable.getName());
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                //更新不是ID的字段，因为根据主键查询的...更新后还是一样。
                if (!column.isId()) {
                    SET(column.getColumn() + "=#{record." + column.getProperty() + "}");
                }
            }
            if (entityTable.getEntityClassPKColumns().size() == 1) {
                EntityHelper.EntityColumn column = entityTable.getEntityClassPKColumns().iterator().next();
                notNullKeyProperty(column.getProperty(), metaObject.getValue(column.getProperty()));
                WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
            } else {
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassPKColumns()) {
                    Object value = metaObject.getValue(column.getProperty());
                    notNullKeyProperty(column.getProperty(), value);
                    WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                }
            }
        }}.toString();
    }

    /**
     * 通过主键更新非空字段
     *
     * @param params
     * @return
     */
    public String updateByPrimaryKeySelective(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            UPDATE(entityTable.getName());
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                Object value = metaObject.getValue(column.getProperty());
                //更新不是ID的字段，因为根据主键查询的...更新后还是一样。
                if (value != null && !column.isId()) {
                    SET(column.getColumn() + "=#{record." + column.getProperty() + "}");
                }
            }
            if (entityTable.getEntityClassPKColumns().size() == 1) {
                EntityHelper.EntityColumn column = entityTable.getEntityClassPKColumns().iterator().next();
                notNullKeyProperty(column.getProperty(), metaObject.getValue(column.getProperty()));
                WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
            } else {
                for (EntityHelper.EntityColumn column : entityTable.getEntityClassPKColumns()) {
                    notNullKeyProperty(column.getProperty(), metaObject.getValue(column.getProperty()));
                    WHERE(column.getColumn() + "=#{record." + column.getProperty() + "}");
                }
            }
        }}.toString();
    }



}
