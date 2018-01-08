/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
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

package tk.mybatis.mapper.provider.base;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.LogicalDelete;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * BaseDeleteMapper实现类，基础方法实现类
 *
 * @author liuzh
 */
public class BaseDeleteProvider extends MapperTemplate {

    public BaseDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过条件删除
     *
     * @param ms
     * @return
     */
    public String delete(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
        return sql.toString();
    }

    /**
     * 通过主键删除
     *
     * @param ms
     */
    public String deleteByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        // 这里查看是否需要逻辑删除
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //对逻辑删除的支持
        EntityColumn logicalDeleteColumn = null;
        for (EntityColumn column : columnList) {
            if (column.getEntityField().isAnnotationPresent(LogicalDelete.class)) {
                if (logicalDeleteColumn != null) {
                    throw new MapperException(entityClass.getCanonicalName() + " 中包含多个带有 @LogicalDelete 注解的字段，一个类中只能存在一个带有 @LogicalDelete 注解的字段!");
                }
                logicalDeleteColumn = column;
            }
        }

        // 如果是逻辑删除就执行 update 否则执行 delete
        if (logicalDeleteColumn != null) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(logicalDeleteColumn.getColumnEqualsHolder());
            sql.append("</set>");
        }else{
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }

        // 删除条件
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }
}
