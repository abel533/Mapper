/*
	The MIT License (MIT)

	Copyright (c) 2014 abel533@gmail.com

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

package com.github.abel533.mapper;

import org.apache.ibatis.builder.StaticSqlSource;
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
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * 处理主要逻辑
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
public class MapperHelper {

    //基础可配置项
    private class Config {
        private String UUID = "";
        private String IDENTITY = "";
        private boolean BEFORE = false;
    }

    private Config config = new Config();

    public void setUUID(String UUID) {
        config.UUID = UUID;
    }

    public void setIDENTITY(String IDENTITY) {
        config.IDENTITY = IDENTITY;
    }

    public void setBEFORE(String BEFORE) {
        config.BEFORE = "BEFORE".equalsIgnoreCase(BEFORE);
    }

    private String getUUID() {
        if (config.UUID != null && config.UUID.length() > 0) {
            return config.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    private String getIDENTITY() {
        if (config.IDENTITY != null && config.IDENTITY.length() > 0) {
            return config.IDENTITY;
        }
        return "CALL IDENTITY()";
    }

    private boolean getBEFORE() {
        return config.BEFORE;
    }

    public static final String DYNAMIC_SQL = "dynamicSQL";
    /**
     * 缓存skip结果
     */
    private final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();
    /**
     * 缓存实体类类型
     */
    private final Map<String, Class<?>> entityType = new HashMap<String, Class<?>>();

    /**
     * 定义要拦截的方法名
     */
    public final String[] METHODS = {
            "select",
            "selectByPrimaryKey",
            "selectCount",
            "insert",
            "insertSelective",
            "delete",
            "deleteByPrimaryKey",
            "updateByPrimaryKey",
            "updateByPrimaryKeySelective"};

    public String dynamicSQL(Object record) {
        return DYNAMIC_SQL;
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
     * 根据msId获取接口类
     *
     * @param msId
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> getMapperClass(String msId) throws ClassNotFoundException {
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        return Class.forName(mapperClassStr);
    }

    /**
     * 接口类是否为Mapper子接口
     *
     * @param mapperClass
     * @return
     */
    public boolean extendsMapper(Class mapperClass) {
        return Mapper.class.isAssignableFrom(mapperClass);
    }

    /**
     * 判断当前的接口方法是否需要进行拦截
     *
     * @param msId
     * @return
     */
    public boolean isMapperMethod(String msId) {
        if (msIdSkip.get(msId) != null) {
            return msIdSkip.get(msId);
        }
        try {
            String methodName = msId.substring(msId.lastIndexOf(".") + 1);
            boolean rightMethod = false;
            for (String method : METHODS) {
                if (method.equals(methodName)) {
                    rightMethod = true;
                    break;
                }
            }
            if (!rightMethod) {
                return false;
            }
            Boolean skip = extendsMapper(getMapperClass(msId));
            msIdSkip.put(msId, skip);
            return skip;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取返回值类型
     *
     * @param ms
     * @return
     */
    public Class<?> getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        if (entityType.get(msId) != null) {
            return entityType.get(msId);
        }
        Class<?> mapperClass = null;
        try {
            mapperClass = getMapperClass(msId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法获取Mapper接口信息:" + msId);
        }
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == Mapper.class) {
                    Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                    entityType.put(msId, returnType);
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
    }

    public String getMethodName(MappedStatement ms) {
        String msId = ms.getId();
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @param sqlSource
     */
    private void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    /**
     * 修改select查询的SqlSource
     *
     * @param ms
     */
    public void selectSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class<?> entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[0])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getSelectSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else if (methodName.equals(METHODS[1])) {//静态sql
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            BEGIN();
            SELECT(EntityHelper.getSelectColumns(entityClass));
            FROM(EntityHelper.getTableName(entityClass));
            WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
            StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
            setSqlSource(ms, sqlSource);
        } else {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getSelectCountSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        }
        if (methodName.equals(METHODS[0]) || methodName.equals(METHODS[1])) {
            ResultMap resultMap = ms.getResultMaps().get(0);
            MetaObject metaObject = MapperHelper.forObject(resultMap);
            metaObject.setValue("type", entityClass);
        }
    }

    /**
     * 修改insert插入的SqlSource
     *
     * @param ms
     */
    public void insertSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class<?> entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[4])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getInsertSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else {//静态sql
            //由于需要selectKey，这里也要改为动态sql
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getInsertAllSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        }
    }

    /**
     * 修改update更新的SqlSource
     *
     * @param ms
     */
    public void updateSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class<?> entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[8])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getUpdateSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else {//静态sql - updateByPrimaryKey
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
    }

    /**
     * 修改delete删除的SqlSource
     *
     * @param ms
     */
    public void deleteSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class<?> entityClass = getSelectReturnType(ms);
        //增加delete
        if (methodName.equals(METHODS[5])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getDeleteSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else {
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            BEGIN();
            DELETE_FROM(EntityHelper.getTableName(entityClass));
            WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
            StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
            setSqlSource(ms, sqlSource);
        }
    }

    /**
     * 根据对象生成主键映射
     *
     * @param ms
     * @return
     */
    private List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
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
     * 根据对象生成所有列的映射
     *
     * @param ms
     * @return
     */
    private List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
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
     * 生成动态select语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getSelectSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
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
     * 生成动态select语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getSelectCountSqlNode(MappedStatement ms) {
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
     * 生成动态insert语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getInsertSqlNode(MappedStatement ms) {
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
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getProperty() + ".nextval ,"), column.getProperty() + " == null "));
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
     * 生成动态insert语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getInsertAllSqlNode(MappedStatement ms) {
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
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getProperty() + ".nextval ,"), column.getProperty() + " == null "));
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
     * 生成动态select语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getDeleteSqlNode(MappedStatement ms) {
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
     * 生成动态select语句
     *
     * @param ms
     * @return MixedSqlNode
     */
    private MixedSqlNode getUpdateSqlNode(MappedStatement ms) {
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

    /**
     * 处理入参
     *
     * @param ms
     * @param args
     */
    public void processParameterObject(MappedStatement ms, Object[] args) {
        Class<?> entityClass = getSelectReturnType(ms);
        String methodName = getMethodName(ms);
        Object parameterObject = args[1];
        //两个通过PK查询的方法用下面的方法处理参数
        if (methodName.equals(METHODS[1]) || methodName.equals(METHODS[6])) {
            TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        if (parameterMappings.size() > 1) {
                            StringBuilder propertyBuilder = new StringBuilder("入参缺少必要的属性错误!参数中必须提供属性:");
                            for (ParameterMapping mapping : parameterMappings) {
                                propertyBuilder.append(mapping.getProperty()).append(",");
                            }
                            throw new RuntimeException(propertyBuilder.substring(0, propertyBuilder.length() - 1));
                        }
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = forObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    parameterMap.put(propertyName, value);
                }
            }
            args[1] = parameterMap;
        } else if (parameterObject == null) {
            throw new RuntimeException("入参不能为空!");
        } else if (!entityClass.isAssignableFrom(parameterObject.getClass())) {
            throw new RuntimeException("入参类型错误，需要的类型为:"
                    + entityClass.getCanonicalName()
                    + ",实际入参类型为:"
                    + parameterObject.getClass().getCanonicalName());
        }
    }

    /**
     * 新建SelectKey节点 - 只对mysql的自动增长有效，Oracle序列直接写到列中
     *
     * @param ms
     * @param column
     */
    private void newSelectKeyMappedStatement(MappedStatement ms, EntityHelper.EntityColumn column) {
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
