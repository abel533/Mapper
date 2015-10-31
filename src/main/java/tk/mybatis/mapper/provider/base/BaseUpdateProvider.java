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
import org.apache.ibatis.scripting.xmltags.*;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

import java.util.ArrayList;
import java.util.List;
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
    public SqlNode updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, "));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new ArrayList<SqlNode>();
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityColumn column : columnList) {
            whereNodes.add(getColumnEqualsProperty(column, first));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 通过主键更新不为null的字段
     *
     * @param ms
     * @return
     */
    public SqlNode updateByPrimaryKeySelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        //全部的if property!=null and property!=''
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, ");
                ifNodes.add(getIfNotNull(column, columnNode));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new ArrayList<SqlNode>();
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityColumn column : columnList) {
            whereNodes.add(getColumnEqualsProperty(column, first));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));
        return new MixedSqlNode(sqlNodes);
    }
}
