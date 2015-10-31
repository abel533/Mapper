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

package tk.mybatis.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/**
 * SpecialProvider实现类，特殊方法实现类
 *
 * @author liuzh
 */
public class SpecialProvider extends MapperTemplate {

    public SpecialProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        EntityTable table = EntityHelper.getEntityTable(entityClass);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(getDynamicTableName(entityClass));
        sql.append("(");
        boolean first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if (!first) {
                sql.append(",");
            }
            sql.append(column.getColumn());
            first = false;
        }
        sql.append(") values ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("(");
        first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if(!first) {
                sql.append(",");
            }
            sql.append(column.getColumnHolder("record"));
            first = false;
        }
        sql.append(")");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 插入，主键id，自增
     *
     * @param ms
     */
    public String insertUseGeneratedKeys(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        EntityTable table = EntityHelper.getEntityTable(entityClass);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(getDynamicTableName(entityClass));
        sql.append("(");
        boolean first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if(!first) {
                sql.append(",");
            }
            sql.append(column.getColumn());
            first = false;
        }
        sql.append(") values(");
        first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if(!first) {
                sql.append(",");
            }
            sql.append(column.getColumnHolder());
            first = false;
        }
        sql.append(")");
        return sql.toString();
    }
}
