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

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper方法
 *
 * @author liuzh
 */
public abstract class MapperTemplate {
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private Class<?> mapperClass;
    private MapperHelper mapperHelper;

    public MapperTemplate(Class<?> mapperClass,MapperHelper mapperHelper) {
        this.mapperClass = mapperClass;
        this.mapperHelper = mapperHelper;
    }

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

    /**
     * 添加映射方法
     *
     * @param methodName
     * @param method
     */
    public void addMethodMap(String methodName, Method method) {
        methodMap.put(methodName, method);
    }

    public String getUUID() {
        return mapperHelper.getUUID();
    }

    public String getIDENTITY() {
        return mapperHelper.getIDENTITY();
    }

    public boolean getBEFORE() {
        return mapperHelper.getBEFORE();
    }

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    /**
     * 反射对象，增加对低版本Mybatis的支持
     *
     * @param object 反射对象
     * @return
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }

    /**
     * 是否支持该通用方法
     *
     * @param msId
     * @return
     */
    public boolean supportMethod(String msId) {
        Class<?> mapperClass = getMapperClass(msId);
        if (this.mapperClass.isAssignableFrom(mapperClass)) {
            String methodName = getMethodName(msId);
            return methodMap.get(methodName) != null;
        }
        return false;
    }

    /**
     * 设置返回值类型
     *
     * @param ms
     * @param entityClass
     */
    protected void setResultType(MappedStatement ms,Class<?> entityClass){
        ResultMap resultMap = ms.getResultMaps().get(0);
        MetaObject metaObject = forObject(resultMap);
        metaObject.setValue("type", entityClass);
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @param sqlSource
     */
    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void setSqlSource(MappedStatement ms) throws Exception {
        Method method = methodMap.get(getMethodName(ms));
        try {
            if (method.getReturnType() == Void.TYPE) {
                method.invoke(this, ms);
            } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                setSqlSource(ms, dynamicSqlSource);
            } else {
                throw new RuntimeException("自定义Mapper方法返回类型错误,可选的返回类型为void和SqlNode!");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException() != null ? e.getTargetException() : e);
        }
    }

    /**
     * 获取返回值类型 - 实体类型
     *
     * @param ms
     * @return
     */
    public Class<?> getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        Class<?> mapperClass = getMapperClass(msId);
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == this.mapperClass) {
                    Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
    }

    /**
     * 根据msId获取接口类
     *
     * @param msId
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getMapperClass(String msId) {
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        try {
            return Class.forName(mapperClassStr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法获取Mapper接口信息:" + msId);
        }
    }

    /**
     * 获取执行的方法名
     *
     * @param ms
     * @return
     */
    public static String getMethodName(MappedStatement ms) {
        return getMethodName(ms.getId());
    }

    /**
     * 获取执行的方法名
     * @param msId
     * @return
     */
    public static String getMethodName(String msId){
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    /**
     * 根据对象生成主键映射
     *
     * @param ms
     * @return
     */
    protected List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        for (EntityHelper.EntityColumn column : entityColumns) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }
        return parameterMappings;
    }

    /**
     * 获取序列下个值的表达式
     *
     * @param column
     * @return
     */
    protected String getSeqNextVal(EntityHelper.EntityColumn column){
        return MessageFormat.format(mapperHelper.getSeqFormat(),column.getSequenceName(),column.getColumn(),column.getProperty());
    }

    /**
     * 获取实体类的表名
     *
     * @param entityClass
     * @return
     */
    protected String tableName(Class<?> entityClass){
        return EntityHelper.getTableName(entityClass);
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property!=null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @return
     */
    protected SqlNode getIfNotNull(EntityHelper.EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + " != null ");
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property==null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @return
     */
    protected SqlNode getIfIsNull(EntityHelper.EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + " == null ");
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property!=null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @return
     */
    protected SqlNode getIfCacheNotNull(EntityHelper.EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache != null ");
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property_cache!=null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @return
     */
    protected SqlNode getIfCacheIsNull(EntityHelper.EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache == null ");
    }

    /**
     * 获取 <code>[AND] column = #{property}</code>
     *
     * @param column
     * @param first
     * @return
     */
    protected SqlNode getColumnEqualsProperty(EntityHelper.EntityColumn column,boolean first){
        return new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
    }

    /**
     * 获取所有列的where节点中的if判断列
     *
     * @param entityClass
     * @return
     */
    protected SqlNode getAllIfColumnNode(Class<?> entityClass){
        //获取全部列
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        //对所有列循环，生成<if test="property!=null">column = #{property}</if>
        for (EntityHelper.EntityColumn column : columnList) {
            ifNodes.add(getIfNotNull(column,getColumnEqualsProperty(column,first)));
            first = false;
        }
        return new MixedSqlNode(ifNodes);
    }

    /**
     * 根据对象生成所有列的映射
     *
     * @param ms
     * @return
     */
    protected List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<EntityHelper.EntityColumn> entityColumns = EntityHelper.getColumns(entityClass);
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        for (EntityHelper.EntityColumn column : entityColumns) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }
        return parameterMappings;
    }

    /**
     * 新建SelectKey节点 - 只对mysql的自动增长有效，Oracle序列直接写到列中
     *
     * @param ms
     * @param column
     */
    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityHelper.EntityColumn column) {
        String keyId = ms.getId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        if (ms.getConfiguration().hasKeyGenerator(keyId)) {
            return;
        }
        Class<?> entityClass = getSelectReturnType(ms);
        //defaults
        Configuration configuration = ms.getConfiguration();
        KeyGenerator keyGenerator = new NoKeyGenerator();
        Boolean executeBefore = getBEFORE();
        String IDENTITY = (column.getGenerator() == null || column.getGenerator().equals("")) ? getIDENTITY() : column.getGenerator();
        SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
        statementBuilder.resource(ms.getResource());
        statementBuilder.fetchSize(null);
        statementBuilder.statementType(StatementType.STATEMENT);
        statementBuilder.keyGenerator(keyGenerator);
        statementBuilder.keyProperty(column.getProperty());
        statementBuilder.keyColumn(null);
        statementBuilder.databaseId(null);
        statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
        statementBuilder.resultOrdered(false);
        statementBuilder.resulSets(null);
        statementBuilder.timeout(configuration.getDefaultStatementTimeout());

        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        ParameterMap.Builder inlineParameterMapBuilder = new ParameterMap.Builder(
                configuration,
                statementBuilder.id() + "-Inline",
                entityClass,
                parameterMappings);
        statementBuilder.parameterMap(inlineParameterMapBuilder.build());

        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                configuration,
                statementBuilder.id() + "-Inline",
                int.class,
                new ArrayList<ResultMapping>(),
                null);
        resultMaps.add(inlineResultMapBuilder.build());
        statementBuilder.resultMaps(resultMaps);
        statementBuilder.resultSetType(null);

        statementBuilder.flushCacheRequired(false);
        statementBuilder.useCache(false);
        statementBuilder.cache(null);

        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);

        MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
        configuration.addKeyGenerator(keyId, new SelectKeyGenerator(keyStatement, executeBefore));
        //keyGenerator
        try {
            MetaObject msObject = forObject(ms);
            msObject.setValue("keyGenerator", configuration.getKeyGenerator(keyId));
        } catch (Exception e) {
            //ignore
        }
    }
}
