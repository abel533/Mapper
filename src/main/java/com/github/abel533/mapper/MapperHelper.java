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
import java.util.*;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * 处理主要逻辑
 *
 * @author liuzh
 */
public class MapperHelper {

    public static final String DYNAMIC_SQL = "dynamicSQL";
    /**
     * 缓存skip结果
     */
    private static final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();
    /**
     * 缓存实体类类型
     */
    private static final Map<String, Class<?>> entityType = new HashMap<String, Class<?>>();

    /**
     * 定义要拦截的方法名
     */
    public static final String[] METHODS = {
            "select",
            "selectByPrimaryKey",
            "selectCount",
            "insert",
            "insertSelective",
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
    public static Class<?> getMapperClass(String msId) throws ClassNotFoundException {
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        return Class.forName(mapperClassStr);
    }

    /**
     * 接口类是否为Mapper子接口
     *
     * @param mapperClass
     * @return
     */
    public static boolean extendsMapper(Class mapperClass) {
        return Mapper.class.isAssignableFrom(mapperClass);
    }

    /**
     * 判断当前的接口方法是否需要进行拦截
     *
     * @param msId
     * @return
     */
    public static boolean isMapperMethod(String msId) {
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
    public static Class<?> getSelectReturnType(MappedStatement ms) {
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

    public static String getMethodName(MappedStatement ms) {
        String msId = ms.getId();
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @param sqlSource
     */
    private static void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    /**
     * 修改select查询的SqlSource
     *
     * @param ms
     */
    public static void selectSqlSource(MappedStatement ms) {
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
    public static void insertSqlSource(MappedStatement ms) {
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
    public static void updateSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class<?> entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[7])) {
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
    public static void deleteSqlSource(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        BEGIN();
        DELETE_FROM(EntityHelper.getTableName(entityClass));
        WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
        setSqlSource(ms, sqlSource);
    }

    /**
     * 根据对象生成主键映射
     *
     * @param ms
     * @return
     */
    private static List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
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
    private static List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
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
    private static MixedSqlNode getSelectSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //select column ... from table
        StaticTextSqlNode selectItems = new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + EntityHelper.getTableName(entityClass));
        sqlNodes.add(selectItems);
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
            first = false;
        }
        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes));

        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成动态select语句
     *
     * @param ms
     * @return
     */
    private static MixedSqlNode getSelectCountSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //select column ... from table
        StaticTextSqlNode selectItems = new StaticTextSqlNode("SELECT COUNT(*) FROM "
                + EntityHelper.getTableName(entityClass));
        sqlNodes.add(selectItems);
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
            first = false;
        }
        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes));

        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成动态insert语句
     *
     * @param ms
     * @return
     */
    private static MixedSqlNode getInsertSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        StaticTextSqlNode insertNode = new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass));
        sqlNodes.add(insertNode);

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        Boolean hasIdentityKey = false;
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                //直接序列加进去
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + ",");
                ifNodes.add(columnNode);
            } else if (column.getIDENTITY()) {
                //列必有
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
            } else if (column.getUUID()) {
                VarDeclSqlNode bind = new VarDeclSqlNode(column.getProperty() + "_bind", "@java.util.UUID@randomUUID()@toString()");
                sqlNodes.add(bind);
            } else {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + ",");
                IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
                ifNodes.add(ifSqlNode);
            }
        }
        TrimSqlNode trimSqlNode = new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ",");
        sqlNodes.add(trimSqlNode);

        ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getProperty() + ".nextval ,");
                ifNodes.add(columnNode);
            } else if (column.getIDENTITY()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode("#{" + column.getProperty() + "_identity },");
                ifNodes.add(columnNode);
            } else if (column.getUUID()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode("#{" + column.getProperty() + "_bind },");
                ifNodes.add(columnNode);
            } else {
                StaticTextSqlNode columnNode = new StaticTextSqlNode("#{" + column.getProperty() + "},");
                IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
                ifNodes.add(ifSqlNode);
            }
        }
        trimSqlNode = new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ",");
        sqlNodes.add(trimSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成动态insert语句
     *
     * @param ms
     * @return
     */
    private static MixedSqlNode getInsertAllSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        StaticTextSqlNode insertNode = new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass));
        sqlNodes.add(insertNode);

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Boolean hasIdentityKey = false;
        //处理Key
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
            } else if (column.getIDENTITY()) {
                //列必有
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
            } else if (column.getUUID()) {
                //TODO 测试这里的UUID
                VarDeclSqlNode bind = new VarDeclSqlNode(column.getProperty() + "_bind", "@java.util.UUID@randomUUID()@toString()");
                sqlNodes.add(bind);
            }
        }
        sqlNodes.add(new StaticTextSqlNode("(" + EntityHelper.getAllColumns(entityClass) + ")"));
        StringBuilder valuesBuilder = new StringBuilder("VALUES (");
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                valuesBuilder.append(column.getProperty() + ".nextval ,");
            } else if (column.getIDENTITY()) {
                valuesBuilder.append("#{" + column.getProperty() + "_identity },");
            } else if (column.getUUID()) {
                valuesBuilder.append("#{" + column.getProperty() + "_bind },");
            } else {
                valuesBuilder.append("#{" + column.getProperty() + "},");
            }
        }
        valuesBuilder.replace(valuesBuilder.length()-1,valuesBuilder.length(),")");
        sqlNodes.add(new StaticTextSqlNode(valuesBuilder.toString()));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成动态select语句
     *
     * @param ms
     * @return MixedSqlNode
     */
    private static MixedSqlNode getUpdateSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //select column ... from table
        StaticTextSqlNode selectItems = new StaticTextSqlNode("UPDATE " + EntityHelper.getTableName(entityClass));
        sqlNodes.add(selectItems);
        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, ");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
        }
        SetSqlNode setSqlNode = new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes));
        sqlNodes.add(setSqlNode);

        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new ArrayList<SqlNode>();
        boolean first = true;
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
            whereNodes.add(columnNode);
            first = false;
        }
        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes));
        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 处理入参
     *
     * @param ms
     * @param args
     */
    public static void processParameterObject(MappedStatement ms, Object[] args) {
        Class<?> entityClass = getSelectReturnType(ms);
        String methodName = getMethodName(ms);
        Object parameterObject = args[1];
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (methodName.equals(METHODS[1]) || methodName.equals(METHODS[5])) {
            TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = forObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    parameterMap.put(propertyName, value);
                }
            }
            args[1] = parameterMap;
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
    private static void newSelectKeyMappedStatement(MappedStatement ms, EntityHelper.EntityColumn column) {
        String keyId = ms.getId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        if (ms.getConfiguration().hasKeyGenerator(keyId)) {
            return;
        }
        Class<?> entityClass = getSelectReturnType(ms);
        //defaults
        Configuration configuration = ms.getConfiguration();
        KeyGenerator keyGenerator = new NoKeyGenerator();
        Boolean executeBefore = true;
        SqlSource sqlSource = new RawSqlSource(configuration, "CALL IDENTITY()", entityClass);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
        statementBuilder.resource(ms.getResource());
        statementBuilder.fetchSize(null);
        statementBuilder.statementType(StatementType.STATEMENT);
        statementBuilder.keyGenerator(keyGenerator);
        statementBuilder.keyProperty(column.getProperty() + "_identity");
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

        statementBuilder.resultMaps(new ArrayList<ResultMap>());
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
