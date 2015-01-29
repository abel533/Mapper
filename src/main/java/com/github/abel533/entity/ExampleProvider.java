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

import java.util.Map;

/**
 * Example查询实现类
 *
 * @author liuzh
 */
public class ExampleProvider extends BaseProvider {

    public String countByExample(final Map<String, Object> params) {
        return new SQL() {{
            MetaObject example = getExample(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            SELECT("count(*)");
            FROM(entityTable.getName());
            applyWhere(this, example);
        }}.toString();
    }

    public String deleteByExample(final Map<String, Object> params) {
        return new SQL() {{
            MetaObject example = getExample(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            DELETE_FROM(entityTable.getName());
            applyWhere(this, example);
        }}.toString();
    }

    public String selectByExample(final Map<String, Object> params) {
        return new SQL() {{
            MetaObject example = getExample(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            SELECT(EntityHelper.getAllColumns(entityClass));
            FROM(entityTable.getName());
            applyWhere(this, example);
            applyOrderBy(this,example);
        }}.toString();
    }

    public String updateByExampleSelective(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            MetaObject example = getExample(params);
            Class<?> entityClass = getEntityClass(params);
            EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            MetaObject metaObject = MapperTemplate.forObject(entity);
            UPDATE(entityTable.getName());
            for (EntityHelper.EntityColumn column : entityTable.getEntityClassColumns()) {
                Object value = metaObject.getValue(column.getProperty());
                //更新不是ID的字段，因为根据主键查询的...更新后还是一样。
                if (value != null) {
                    SET(column.getColumn() + "=#{record." + column.getProperty() + "}");
                }
            }
            applyWhere(this, example);
        }}.toString();
    }

    public String updateByExample(final Map<String, Object> params) {
        return new SQL() {{
            Object entity = getEntity(params);
            MetaObject example = getExample(params);
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
            applyWhere(this, example);
        }}.toString();
    }
}
