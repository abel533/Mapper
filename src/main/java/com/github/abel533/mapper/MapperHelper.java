package com.github.abel533.mapper;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzh on 2014/11/19.
 */
public class MapperHelper {
    /**
     * 定义要拦截的方法名
     */
    public static final String[] METHODS = {
            "select",
            "selectByPrimaryKey",
            "insert",
            "insertSelective",
            "deleteByPrimaryKey",
            "updateByPrimaryKey",
            "updateByPrimaryKeySelective"};

    public String dynamicSQL(Object record) {
        return "select * from country where id = " + record;
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
            return extendsMapper(getMapperClass(msId));
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
        //动态sql
        if (methodName.equals(METHODS[0])) {
            //TODO 根据属性生成if节点
            List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
            SqlNode sqlNode = new MixedSqlNode(sqlNodes);
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
            setSqlSource(ms, dynamicSqlSource);

        } else {//静态sql - selectByPrimaryKey
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            //根据parameterMappings构造入参parameterObject
            StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), "select * from country where id = ?", parameterMappings);
            setSqlSource(ms, sqlSource);
        }
    }

    public static void insertSqlSource(MappedStatement ms) {
        List<ParameterMapping> parameterMappings = getColumnParameterMappings(ms);
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), "sql", parameterMappings);
        setSqlSource(ms, sqlSource);
    }

    public static void updateSqlSource(MappedStatement ms) {
        String methodName = getMethodName(ms);
        //动态sql
        if (methodName.equals(METHODS[6])) {
            //TODO 根据属性生成if节点
            List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
            SqlNode sqlNode = new MixedSqlNode(sqlNodes);
            DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
            setSqlSource(ms, dynamicSqlSource);

        } else {//静态sql - updateByPrimaryKey
            List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
            StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), "sql", parameterMappings);
            setSqlSource(ms, sqlSource);
        }
    }

    public static void deleteSqlSource(MappedStatement ms) {
        List<ParameterMapping> parameterMappings = getColumnParameterMappings(ms);
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), "sql", parameterMappings);
        setSqlSource(ms, sqlSource);
    }

    /**
     * 根据对象生成主键映射
     *
     * @param ms
     * @return
     */
    private static List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
        Class T = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        //TODO 联合主键可以写多个
        ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(),"id",int.class);
        builder.mode(ParameterMode.IN);
        parameterMappings.add(builder.build());
        return parameterMappings;
    }

    /**
     * 根据对象生成所有列的映射
     *
     * @param ms
     * @return
     */
    private static List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
        Class T = getSelectReturnType(ms);
        //TODO
        return null;
    }

    /**
     * 根据对象所有列生成if的动态sqlNode
     *
     * @param ms
     * @return
     */
    private static MixedSqlNode getSqlNode(MappedStatement ms){
        Class T = getSelectReturnType(ms);
        //
//        IfSqlNode ifSqlNode = new IfSqlNode()
        StaticTextSqlNode textSqlNode = new StaticTextSqlNode("");
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>(1);
        sqlNodes.add(textSqlNode);
        MixedSqlNode mixedSqlNode = new MixedSqlNode(sqlNodes);
        IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode,"property!=null and propertye!=''");
        return null;
    }
}
