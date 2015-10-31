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
import org.apache.ibatis.scripting.xmltags.*;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ExampleProvider实现类，基础方法实现类
 *
 * @author liuzh
 */
public class ExampleProvider extends MapperTemplate {

    public ExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public SqlNode selectCountByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT COUNT(*) FROM "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据Example删除
     *
     * @param ms
     * @return
     */
    public SqlNode deleteByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("DELETE FROM "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }


    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public SqlNode selectByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT"));
        IfSqlNode distinctSqlNode = new IfSqlNode(new StaticTextSqlNode("DISTINCT"), "distinct");
        sqlNodes.add(distinctSqlNode);

        ForEachSqlNode forEachSelectColumns = new ForEachSqlNode(ms.getConfiguration(), new TextSqlNode("${selectColumn}"), "_parameter.selectColumns", null, "selectColumn", null, null, ",");
        IfSqlNode ifSelectColumns = new IfSqlNode(forEachSelectColumns, "@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)");
        sqlNodes.add(ifSelectColumns);

        IfSqlNode ifNoSelectColumns = new IfSqlNode(new StaticTextSqlNode(EntityHelper.getSelectColumns(entityClass)), "@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)");
        sqlNodes.add(ifNoSelectColumns);

        sqlNodes.add(new StaticTextSqlNode(" FROM "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        IfSqlNode orderByClauseSqlNode = new IfSqlNode(new TextSqlNode("order by ${orderByClause}"), "orderByClause != null");
        sqlNodes.add(orderByClauseSqlNode);
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            IfSqlNode defaultOrderByClauseSqlNode = new IfSqlNode(new StaticTextSqlNode("ORDER BY " + orderByClause), "orderByClause == null");
            sqlNodes.add(defaultOrderByClauseSqlNode);
        }
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public SqlNode selectByExampleAndRowBounds(MappedStatement ms) {
        return selectByExample(ms);
    }

    /**
     * 根据Example更新非null字段
     *
     * @param ms
     * @return
     */
    public SqlNode updateByExampleSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE "));
        sqlNodes.add(getDynamicTableNameNode(entityClass, "record"));

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();

        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{record." + column.getProperty() + "}, ");
                ifNodes.add(new IfSqlNode(columnNode, "record." + column.getProperty() + " != null"));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //Example的Where
        IfSqlNode ifNullSqlNode = new IfSqlNode(updateByExampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据Example更新
     *
     * @param ms
     * @return
     */
    public SqlNode updateByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE "));
        sqlNodes.add(getDynamicTableNameNode(entityClass, "record"));

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> setSqlNodes = new ArrayList<SqlNode>();
        //全部的if property!=null and property!=''
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                setSqlNodes.add(new StaticTextSqlNode(column.getColumn() + " = #{record." + column.getProperty() + "}, "));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(setSqlNodes)));
        //Example的Where
        IfSqlNode ifNullSqlNode = new IfSqlNode(updateByExampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }
}
