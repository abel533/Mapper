package com.github.abel533.mapper;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by liuzh on 2014/11/19.
 */
public class MapperHelper {

    public static final String DYNAMIC_SQL = "dynamicSQL";
    /**
     * 缓存skip结果
     */
    private static final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();

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
    public static Class getMapperClass(String msId) throws ClassNotFoundException {
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
    public static Class getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        Class mapperClass = null;
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
                    return (Class) t.getActualTypeArguments()[0];
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
    }

    public static String getMethodName(MappedStatement ms) {
        String msId = ms.getId();
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    private static void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    public static void selectSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[0])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getSelectSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else if (methodName.equals(METHODS[1])) {//静态sql - selectByPrimaryKey
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

    public static void insertSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class entityClass = getSelectReturnType(ms);
        //动态sql
        if (methodName.equals(METHODS[4])) {
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getInsertSqlNode(ms));
            setSqlSource(ms, dynamicSqlSource);
        } else {//静态sql - selectByPrimaryKey
            List<ParameterMapping> parameterMappings = getColumnParameterMappings(ms);
            BEGIN();
            INSERT_INTO(EntityHelper.getTableName(entityClass));
            List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
            for (EntityHelper.EntityColumn column : columnList) {
                VALUES(column.getColumn(), "?");
            }
            StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), SQL(), parameterMappings);
            setSqlSource(ms, sqlSource);
        }
    }

    public static void updateSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        Class entityClass = getSelectReturnType(ms);
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

    public static void deleteSqlSource(MappedStatement ms) {
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        StaticTextSqlNode insertNode = new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass));
        sqlNodes.add(insertNode);

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + ",");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
        }
        TrimSqlNode trimSqlNode = new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ",");
        sqlNodes.add(trimSqlNode);

        ifNodes = new ArrayList<SqlNode>();
        for (EntityHelper.EntityColumn column : columnList) {
            StaticTextSqlNode columnNode = new StaticTextSqlNode("#{" + column.getProperty() + "},");
            IfSqlNode ifSqlNode = new IfSqlNode(columnNode, column.getProperty() + " != null ");
            ifNodes.add(ifSqlNode);
        }
        trimSqlNode = new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ",");
        sqlNodes.add(trimSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成动态select语句
     *
     * @param ms
     * @return
     */
    private static MixedSqlNode getUpdateSqlNode(MappedStatement ms) {
        Class entityClass = getSelectReturnType(ms);
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
        Class entityClass = getSelectReturnType(ms);
        String methodName = getMethodName(ms);
        Object parameterObject = args[1];
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (methodName.equals(METHODS[1]) || methodName.equals(METHODS[5])) {
            TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
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
}
