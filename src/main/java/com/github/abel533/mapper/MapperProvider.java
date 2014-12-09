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

package com.github.abel533.mapper;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * @author liuzh
 */
public class MapperProvider extends MapperTemplate {

    public MapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 需要处理主键的情况
     *
     * @param methodName
     * @return
     */
    @Override
    public boolean processPKParameter(String methodName) {
        return methodName.equals("selectByPrimaryKey") || methodName.equals("deleteByPrimaryKey");
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public SqlNode select(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + EntityHelper.getTableName(entityClass)));
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据主键进行查询
     *
     * @param ms
     */
    public void selectByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        BEGIN();
        SELECT(EntityHelper.getSelectColumns(entityClass));
        FROM(EntityHelper.getTableName(entityClass));
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        setSqlSource(ms, sqlSource);
        setResultType(ms, entityClass);
    }

    /**
     * 查询总数
     *
     * @param ms
     * @return
     */
    public SqlNode selectCount(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT COUNT(*) FROM "
                + EntityHelper.getTableName(entityClass)));
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 插入全部
     *
     * @param ms
     * @return
     */
    public SqlNode insert(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass)));

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Boolean hasIdentityKey = false;
        //处理Key
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
            } else if (column.isIdentity()) {
                //列必有
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
            } else if (column.isUuid()) {
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
            }
        }
        sqlNodes.add(new StaticTextSqlNode("(" + EntityHelper.getAllColumns(entityClass) + ")"));
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            //优先使用传入的属性值
            //自增的情况下,如果默认有值,就会备份到property_cache中
            if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_cache },"), column.getProperty() + "_cache != null "));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " != null "));
            }

            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getSequenceName() + ".nextval ,"), column.getProperty() + " == null "));
            } else if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + " },"), column.getProperty() + "_cache == null "));
            } else if (column.isUuid()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_bind },"), column.getProperty() + " == null "));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " == null "));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 插入不为null的字段
     *
     * @param ms
     * @return
     */
    public SqlNode insertSelective(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass)));

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        Boolean hasIdentityKey = false;
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                //直接序列加进去
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else if (column.isIdentity()) {
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //新增一个selectKey-MS
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
            } else if (column.isUuid()) {
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getColumn() + ","), column.getProperty() + " != null "));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ","));

        ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            //当参数中的属性值不为空的时候,使用传入的值
            //自增的情况下,如果默认有值,就会备份到property_cache中
            if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_cache },"), column.getProperty() + "_cache != null "));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " != null "));
            }
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getSequenceName() + ".nextval ,"), column.getProperty() + " == null "));
            } else if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + " },"), column.getProperty() + " == null "));
            } else if (column.isUuid()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_bind },"), column.getProperty() + " == null "));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 通过条件删除
     *
     * @param ms
     * @return
     */
    public SqlNode delete(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        sqlNodes.add(new StaticTextSqlNode("DELETE FROM " + EntityHelper.getTableName(entityClass)));
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            ifNodes.add(new IfSqlNode(columnNode, column.getProperty() + " != null "));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 通过主键删除
     *
     * @param ms
     */
    public void deleteByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        BEGIN();
        DELETE_FROM(EntityHelper.getTableName(entityClass));
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        setSqlSource(ms, sqlSource);
    }

    /**
     * 通过主键更新全部字段
     *
     * @param ms
     */
    public void updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //映射要包含set=?和where=?
        List<ParameterMapping> parameterMappings = getColumnParameterMappings(ms);
        parameterMappings.addAll(getPrimaryKeyParameterMappings(ms));
        BEGIN();
        UPDATE(EntityHelper.getTableName(entityClass));
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityHelper.EntityColumn column : columnList) {
            SET(column.getColumn() + " = ?");
        }
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        setSqlSource(ms, sqlSource);
    }

    /**
     * 通过主键更新不为null的字段
     *
     * @param ms
     * @return
     */
    public SqlNode updateByPrimaryKeySelective(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        sqlNodes.add(new StaticTextSqlNode("UPDATE " + EntityHelper.getTableName(entityClass)));
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, ");
            ifNodes.add(new IfSqlNode(columnNode, column.getProperty() + " != null "));
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));

        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            whereNodes.add(new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} "));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));
        return new MixedSqlNode(sqlNodes);
    }
}
