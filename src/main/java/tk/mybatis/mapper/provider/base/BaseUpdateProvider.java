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

package tk.mybatis.mapper.provider.base;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * BaseUpdateProvider实现类，基础方法实现类
 *
 * @author liuzh
 */
public class BaseUpdateProvider extends MapperTemplate {

    public BaseUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过主键更新全部字段
     *
     * @param ms
     */
    public String updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, tableName(entityClass)));
        sql.append(" SET ");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                sql.append(column.getColumnEqualsHolder()).append(",");
            }
        }
        if (sql.charAt(sql.length() - 1) == ',') {
            sql.setCharAt(sql.length() - 1, ' ');
        }
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        sql.append(" WHERE ");
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityColumn column : columnList) {
            sql.append(SqlHelper.getColumnEqualsProperty(column, first));
            first = false;
        }
        return sql.toString();
    }

    /**
     * 通过主键更新不为null的字段
     *
     * @param ms
     * @return
     */
    public String updateByPrimaryKeySelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, tableName(entityClass)));
        sql.append(" <set> ");

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //全部的if property!=null and property!=''
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnEqualsHolder() + ",", isNotEmpty()));
            }
        }
        sql.append(" </set> ");
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        sql.append(" WHERE ");
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityColumn column : columnList) {
            sql.append(SqlHelper.getColumnEqualsProperty(column, first));
            first = false;
        }
        return sql.toString();
    }
}
