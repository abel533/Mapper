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

package tk.mybatis.mapper.mapperhelper;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

/**
 * 通用Mapper模板类，扩展通用Mapper时需要继承该类
 *
 * @author liuzh
 */
public abstract class MapperTemplate {
    private XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private Class<?> mapperClass;
    private MapperHelper mapperHelper;

    public MapperTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
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

    /**
     * 是否支持该通用方法
     *
     * @param msId
     * @return
     */
    public boolean supportMethod(String msId) {
        Class<?> mapperClass = getMapperClass(msId);
        if (mapperClass != null && this.mapperClass.isAssignableFrom(mapperClass)) {
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
    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        ResultMap resultMap = ms.getResultMaps().get(0);
        MetaObject metaObject = SystemMetaObject.forObject(resultMap);
        metaObject.setValue("type", entityClass);
    }

    /**
     * 重新设置SqlSource，同时判断如果是Jdbc3KeyGenerator，就设置为MultipleJdbc3KeyGenerator
     *
     * @param ms
     * @param sqlSource
     */
    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = SystemMetaObject.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
        //如果是Jdbc3KeyGenerator，就设置为MultipleJdbc3KeyGenerator
        KeyGenerator keyGenerator = ms.getKeyGenerator();
        if(keyGenerator instanceof Jdbc3KeyGenerator){
            msObject.setValue("keyGenerator", new MultipleJdbc3KeyGenerator());
        }
    }

    /**
     * check ms cache
     *
     * @param ms
     * @throws Exception
     */
    private void checkCache(MappedStatement ms) throws Exception {
        if(ms.getCache() == null){
            String nameSpace = ms.getId().substring(0, ms.getId().lastIndexOf("."));
            Cache cache = null;
            try {
                //不存在的时候会抛出异常
                cache = ms.getConfiguration().getCache(nameSpace);
            } catch (IllegalArgumentException e) {
                return;
            }
            if(cache != null){
                MetaObject metaObject = SystemMetaObject.forObject(ms);
                metaObject.setValue("cache", cache);
            }
        }
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public void setSqlSource(MappedStatement ms) throws Exception {
        if (this.mapperClass == getMapperClass(ms.getId())) {
            if (mapperHelper.isSpring4()) {
                return;
            } else if (mapperHelper.isSpring()) {
                throw new RuntimeException("Spring4.x.x 及以上版本支持泛型注入," +
                        "您当前的Spring版本为" + mapperHelper.getSpringVersion() + ",不能使用泛型注入," +
                        "因此在配置MapperScannerConfigurer时,不要扫描通用Mapper接口类," +
                        "也不要在您Mybatis的xml配置文件中的<mappers>中指定通用Mapper接口类.");
            } else {
                throw new RuntimeException("请不要在您Mybatis的xml配置文件中的<mappers>中指定通用Mapper接口类.");
            }
        }
        Method method = methodMap.get(getMethodName(ms));
        try {
            if (method.getReturnType() == Void.TYPE) {
                method.invoke(this, ms);
            } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                setSqlSource(ms, dynamicSqlSource);
            } else if (String.class.equals(method.getReturnType())) {
                String xmlSql = (String) method.invoke(this, ms);
                SqlSource sqlSource = createSqlSource(ms, xmlSql);
                //替换原有的SqlSource
                setSqlSource(ms, sqlSource);
            } else {
                throw new RuntimeException("自定义Mapper方法返回类型错误,可选的返回类型为void,SqlNode,String三种!");
            }
            //cache
            checkCache(ms);
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
                if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class<?>) t.getRawType())) {
                    Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
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
        if (msId.indexOf(".") == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        }
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        try {
            return Class.forName(mapperClassStr);
        } catch (ClassNotFoundException e) {
            return null;
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
     *
     * @param msId
     * @return
     */
    public static String getMethodName(String msId) {
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
        Set<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        List<ParameterMapping> parameterMappings = new LinkedList<ParameterMapping>();
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
    protected String getSeqNextVal(EntityHelper.EntityColumn column) {
        return MessageFormat.format(mapperHelper.getSeqFormat(), column.getSequenceName(), column.getColumn(), column.getProperty());
    }

    /**
     * 获取实体类的表名
     *
     * @param entityClass
     * @return
     */
    protected String tableName(Class<?> entityClass) {
        return mapperHelper.getTableName(entityClass);
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property!=null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @param columnNode
     * @return
     */
    protected SqlNode getIfNotNull(EntityHelper.EntityColumn column, SqlNode columnNode) {
        return getIfNotNull(column, columnNode, false);
    }

    /**
     * 返回if条件的sqlNode
     * <p>一般类型：<code>&lt;if test="property!=null"&gt;columnNode&lt;/if&gt;</code></p>
     *
     * @param column
     * @param columnNode
     * @param empty      是否包含!=''条件
     * @return
     */
    protected SqlNode getIfNotNull(EntityHelper.EntityColumn column, SqlNode columnNode, boolean empty) {
        if (empty && column.getJavaType().equals(String.class)) {
            return new IfSqlNode(columnNode, column.getProperty() + " != null and " + column.getProperty() + " != ''");
        } else {
            return new IfSqlNode(columnNode, column.getProperty() + " != null ");
        }
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
    protected SqlNode getColumnEqualsProperty(EntityHelper.EntityColumn column, boolean first) {
        return new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
    }

    /**
     * 获取所有列的where节点中的if判断列
     *
     * @param entityClass
     * @return
     */
    protected SqlNode getAllIfColumnNode(Class<?> entityClass) {
        //获取全部列
        Set<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<SqlNode>();
        boolean first = true;
        //对所有列循环，生成<if test="property!=null">column = #{property}</if>
        for (EntityHelper.EntityColumn column : columnList) {
            ifNodes.add(getIfNotNull(column, getColumnEqualsProperty(column, first), mapperHelper.isNotEmpty()));
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
        Set<EntityHelper.EntityColumn> entityColumns = EntityHelper.getColumns(entityClass);
        List<ParameterMapping> parameterMappings = new LinkedList<ParameterMapping>();
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
        KeyGenerator keyGenerator;
        Boolean executeBefore = getBEFORE();
        String IDENTITY = (column.getGenerator() == null || column.getGenerator().equals("")) ? getIDENTITY() : column.getGenerator();
        if (IDENTITY.equalsIgnoreCase("JDBC")) {
            keyGenerator = new Jdbc3KeyGenerator();
        } else {
            SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);

            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
            statementBuilder.resource(ms.getResource());
            statementBuilder.fetchSize(null);
            statementBuilder.statementType(StatementType.STATEMENT);
            statementBuilder.keyGenerator(new NoKeyGenerator());
            statementBuilder.keyProperty(column.getProperty());
            statementBuilder.keyColumn(null);
            statementBuilder.databaseId(null);
            statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
            statementBuilder.resultOrdered(false);
            statementBuilder.resulSets(null);
            statementBuilder.timeout(configuration.getDefaultStatementTimeout());

            List<ParameterMapping> parameterMappings = new LinkedList<ParameterMapping>();
            ParameterMap.Builder inlineParameterMapBuilder = new ParameterMap.Builder(
                    configuration,
                    statementBuilder.id() + "-Inline",
                    entityClass,
                    parameterMappings);
            statementBuilder.parameterMap(inlineParameterMapBuilder.build());

            List<ResultMap> resultMaps = new LinkedList<ResultMap>();
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                    configuration,
                    statementBuilder.id() + "-Inline",
                    column.getJavaType(),
                    new LinkedList<ResultMapping>(),
                    null);
            resultMaps.add(inlineResultMapBuilder.build());
            statementBuilder.resultMaps(resultMaps);
            statementBuilder.resultSetType(null);

            statementBuilder.flushCacheRequired(false);
            statementBuilder.useCache(false);
            statementBuilder.cache(null);

            MappedStatement statement = statementBuilder.build();
            try {
                configuration.addMappedStatement(statement);
            } catch (Exception e) {
                //ignore
            }
            MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
            keyGenerator = new SelectKeyGenerator(keyStatement, executeBefore);
            try {
                configuration.addKeyGenerator(keyId, keyGenerator);
            } catch (Exception e) {
                //ignore
            }
        }
        //keyGenerator
        try {
            MetaObject msObject = SystemMetaObject.forObject(ms);
            msObject.setValue("keyGenerator", keyGenerator);
            msObject.setValue("keyProperties", column.getTable().getKeyProperties());
            msObject.setValue("keyColumns", column.getTable().getKeyColumns());
        } catch (Exception e) {
            //ignore
        }
    }

    public IfSqlNode ExampleValidSqlNode(Configuration configuration) {
        List<SqlNode> whenSqlNodes = new LinkedList<SqlNode>();
        IfSqlNode noValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition}"), "criterion.noValue");
        whenSqlNodes.add(noValueSqlNode);
        IfSqlNode singleValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value}"), "criterion.singleValue");
        whenSqlNodes.add(singleValueSqlNode);
        IfSqlNode betweenValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}"), "criterion.betweenValue");
        whenSqlNodes.add(betweenValueSqlNode);

        List<SqlNode> listValueContentSqlNodes = new LinkedList<SqlNode>();
        listValueContentSqlNodes.add(new TextSqlNode(" and ${criterion.condition}"));
        ForEachSqlNode listValueForEachSqlNode = new ForEachSqlNode(configuration, new StaticTextSqlNode("#{listItem}"), "criterion.value", null, "listItem", "(", ")", ",");
        listValueContentSqlNodes.add(listValueForEachSqlNode);
        IfSqlNode listValueSqlNode = new IfSqlNode(new MixedSqlNode(listValueContentSqlNodes), "criterion.listValue");
        whenSqlNodes.add(listValueSqlNode);

        ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, null);

        ForEachSqlNode criteriaSqlNode = new ForEachSqlNode(configuration, chooseSqlNode, "criteria.criteria", null, "criterion", null, null, null);

        TrimSqlNode trimSqlNode = new TrimSqlNode(configuration, criteriaSqlNode, "(", "and", ")", null);
        IfSqlNode validSqlNode = new IfSqlNode(trimSqlNode, "criteria.valid");
        return validSqlNode;
    }

    /**
     * Example查询中的where结构
     *
     * @param configuration
     * @return
     */
    public WhereSqlNode exampleWhereClause(Configuration configuration) {
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, ExampleValidSqlNode(configuration), "oredCriteria", null, "criteria", null, null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }

    /**
     * Example-Update中的where结构
     *
     * @param configuration
     * @return
     */
    public WhereSqlNode updateByExampleWhereClause(Configuration configuration) {
        //和上面方法的区别就在"example.oredCriteria"
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, ExampleValidSqlNode(configuration), "example.oredCriteria", null, "criteria", null, null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }

    public SqlSource createSqlSource(MappedStatement ms, String xmlSql) {
        return languageDriver.createSqlSource(ms.getConfiguration(), "<script>\n\t" + xmlSql + "</script>", null);
    }
}