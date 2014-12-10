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
 * Mappper实现类
 *
 * @author liuzh
 */
public class MapperProvider extends MapperTemplate {

    public MapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public SqlNode select(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + tableName(entityClass)));
        //将if添加到<where>
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), getAllIfColumnNode(entityClass)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据主键进行查询
     *
     * @param ms
     */
    public void selectByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //获取主键字段映射
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        //开始拼sql
        BEGIN();
        //select全部列
        SELECT(EntityHelper.getSelectColumns(entityClass));
        //from表
        FROM(tableName(entityClass));
        //where条件，主键字段=#{property}
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        //SQL()方法获取最终SQL，使用静态SqlSource
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        //替换原有的SqlSource
        setSqlSource(ms, sqlSource);
        //将返回值修改为实体类型
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
        //select count(*) from table
        sqlNodes.add(new StaticTextSqlNode("SELECT COUNT(*) FROM " + tableName(entityClass)));
        //获取全部列的where,if条件
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), getAllIfColumnNode(entityClass)));
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
        //insert into table
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //处理所有的主键策略
        for (EntityHelper.EntityColumn column : columnList) {
            //序列的情况，直接写入sql中，不需要额外的获取值
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
            } else if (column.isIdentity()) {
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
            } else if (column.isUuid()) {
                //uuid的情况，直接插入bind节点
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
            }
        }
        //插入全部的(列名,列名...)
        sqlNodes.add(new StaticTextSqlNode("(" + EntityHelper.getAllColumns(entityClass) + ")"));
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        //处理所有的values(属性值,属性值...)
        for (EntityHelper.EntityColumn column : columnList) {
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                ifNodes.add(getIfCacheNotNull(column,new StaticTextSqlNode("#{" + column.getProperty() + "_cache },")));
            } else {
                //其他情况值仍然存在原property中
                ifNodes.add(getIfNotNull(column,new StaticTextSqlNode("#{" + column.getProperty() + "},")));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                ifNodes.add(getIfIsNull(column, new StaticTextSqlNode(getSeqNextVal(column) + " ,")));
            } else if (column.isIdentity()) {
                ifNodes.add(getIfCacheIsNull(column, new StaticTextSqlNode("#{" + column.getProperty() + " },")));
            } else if (column.isUuid()) {
                ifNodes.add(getIfIsNull(column, new StaticTextSqlNode("#{" + column.getProperty() + "_bind },")));
            } else {
                //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                ifNodes.add(getIfIsNull(column, new StaticTextSqlNode("#{" + column.getProperty() + ",jdbcType=VARCHAR},")));
            }
        }
        //values(#{property},#{property}...)
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
        //insert into table
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityHelper.EntityColumn column : columnList) {
            //当使用序列时
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                //直接将列加进去
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else if (column.isIdentity()) {
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //新增一个selectKey-MS
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
                //加入该列
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
            } else if (column.isUuid()) {
                //将UUID的值加入bind节点
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else {
                ifNodes.add(getIfNotNull(column,new StaticTextSqlNode(column.getColumn() + ",")));
            }
        }
        //将动态的列加入sqlNodes
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ","));

        ifNodes = new ArrayList<SqlNode>();
        //处理values(#{property},#{property}...)
        for (EntityHelper.EntityColumn column : columnList) {
            //当参数中的属性值不为空的时候,使用传入的值
            //自增的情况下,如果默认有值,就会备份到property_cache中
            if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_cache },"), column.getProperty() + "_cache != null "));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " != null "));
            }
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                ifNodes.add(getIfIsNull(column, new StaticTextSqlNode(getSeqNextVal(column) + " ,")));
            } else if (column.isIdentity()) {
                ifNodes.add(getIfCacheIsNull(column, new StaticTextSqlNode("#{" + column.getProperty() + " },")));
            } else if (column.isUuid()) {
                ifNodes.add(getIfIsNull(column, new StaticTextSqlNode("#{" + column.getProperty() + "_bind },")));
            }
        }
        //values(#{property},#{property}...)
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
        //delete from table
        sqlNodes.add(new StaticTextSqlNode("DELETE FROM " + tableName(entityClass)));
        //where/if判断条件
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), getAllIfColumnNode(entityClass)));
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
        //开始拼sql
        BEGIN();
        //delete from table
        DELETE_FROM(tableName(entityClass));
        //where 主键=#{property} 条件
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        //静态SqlSource
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        //替换原有的SqlSource
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
        //获取set映射
        List<ParameterMapping> parameterMappings = getColumnParameterMappings(ms);
        //获取where主键映射
        parameterMappings.addAll(getPrimaryKeyParameterMappings(ms));
        //开始拼Sql
        BEGIN();
        //update table
        UPDATE(tableName(entityClass));
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //拼所有的set column = ?
        for (EntityHelper.EntityColumn column : columnList) {
            SET(column.getColumn() + " = ?");
        }
        //where 主键=#{property} 条件
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        //静态SqlSource
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        //替换原有的SqlSource
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
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE " + tableName(entityClass)));
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        //全部的if property!=null and property!=''
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, ");
            ifNodes.add(getIfNotNull(column,columnNode));
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new ArrayList<SqlNode>();
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityHelper.EntityColumn column : columnList) {
            whereNodes.add(getColumnEqualsProperty(column,first));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));
        return new MixedSqlNode(sqlNodes);
    }
}
