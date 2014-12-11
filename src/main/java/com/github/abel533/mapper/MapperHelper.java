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

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 处理主要逻辑
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
public class MapperHelper {

    /**
     * 注册的通用Mapper接口
     */
    private Map<Class<?>, MapperTemplate> registerMapper = new HashMap<Class<?>, MapperTemplate>();

    /**
     * 缓存msid和MapperTemplate
     */
    private Map<String, MapperTemplate> msIdCache = new HashMap<String, MapperTemplate>();
    /**
     * 缓存skip结果
     */
    private final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();

    /**
     * 通过通用Mapper接口获取对应的MapperTemplate
     *
     * @param mapperClass
     * @return
     * @throws Exception
     */
    private MapperTemplate fromMapperClass(Class<?> mapperClass) {
        Method[] methods = mapperClass.getDeclaredMethods();
        Class<?> templateClass = null;
        Class<?> tempClass = null;
        Set<String> methodSet = new HashSet<String>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SelectProvider.class)) {
                SelectProvider provider = method.getAnnotation(SelectProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }
            else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }
            else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }
            else if (method.isAnnotationPresent(UpdateProvider.class)) {
                UpdateProvider provider = method.getAnnotation(UpdateProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            }
            if (templateClass == null) {
                templateClass = tempClass;
            } else if (templateClass != tempClass) {
                throw new RuntimeException("一个通用Mapper中只允许存在一个MapperTemplate子类!");
            }
        }
        if (templateClass == null || !MapperTemplate.class.isAssignableFrom(templateClass)) {
            throw new RuntimeException("接口中不存在包含type为MapperTemplate的Provider注解，这不是一个合法的通用Mapper接口类!");
        }
        MapperTemplate mapperTemplate = null;
        try {
            mapperTemplate = (MapperTemplate) templateClass.getConstructor(Class.class, MapperHelper.class).newInstance(mapperClass, this);
        } catch (Exception e) {
            throw new RuntimeException("实例化MapperTemplate对象失败:" + e.getMessage());
        }
        //注册方法
        for (String methodName : methodSet) {
            try {
                mapperTemplate.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
            }
        }
        return mapperTemplate;
    }

    /**
     * 注册通用Mapper接口
     *
     * @param mapperClass
     * @throws Exception
     */
    public void registerMapper(Class<?> mapperClass)  {
        if (registerMapper.get(mapperClass) == null) {
            registerMapper.put(mapperClass,fromMapperClass(mapperClass));
        } else {
            throw new RuntimeException("已经注册过的通用Mapper[" + mapperClass.getCanonicalName() + "]不能多次注册!");
        }
    }

    /**
     * 注册通用Mapper接口
     *
     * @param mapperClass
     * @throws Exception
     */
    public void registerMapper(String mapperClass) {
        try {
            registerMapper(Class.forName(mapperClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("注册通用Mapper["+mapperClass+"]失败，找不到该通用Mapper!");
        }
    }

    /**
     * IDENTITY的可选值
     */
    public enum IdentityDialect {
        DB2("VALUES IDENTITY_VAL_LOCAL()"),
        MYSQL("SELECT LAST_INSERT_ID()"),
        SQLSERVER("SELECT SCOPE_IDENTITY()"),
        CLOUDSCAPE("VALUES IDENTITY_VAL_LOCAL()"),
        DERBY("VALUES IDENTITY_VAL_LOCAL()"),
        HSQLDB("CALL IDENTITY()"),
        SYBASE("SELECT @@IDENTITY"),
        DB2_MF("SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1"),
        INFORMIX("select dbinfo('sqlca.sqlerrd1') from systables where tabid=1");

        private String identityRetrievalStatement;

        private IdentityDialect(String identityRetrievalStatement) {
            this.identityRetrievalStatement = identityRetrievalStatement;
        }

        public String getIdentityRetrievalStatement() {
            return identityRetrievalStatement;
        }

        public static IdentityDialect getDatabaseDialect(String database) {
            IdentityDialect returnValue = null;
            if ("DB2".equalsIgnoreCase(database)) {
                returnValue = DB2;
            } else if ("MySQL".equalsIgnoreCase(database)) {
                returnValue = MYSQL;
            } else if ("SqlServer".equalsIgnoreCase(database)) {
                returnValue = SQLSERVER;
            } else if ("Cloudscape".equalsIgnoreCase(database)) {
                returnValue = CLOUDSCAPE;
            } else if ("Derby".equalsIgnoreCase(database)) {
                returnValue = DERBY;
            } else if ("HSQLDB".equalsIgnoreCase(database)) {
                returnValue = HSQLDB;
            } else if ("SYBASE".equalsIgnoreCase(database)) {
                returnValue = SYBASE;
            } else if ("DB2_MF".equalsIgnoreCase(database)) {
                returnValue = DB2_MF;
            } else if ("Informix".equalsIgnoreCase(database)) {
                returnValue = INFORMIX;
            }
            return returnValue;
        }
    }

    //基础可配置项
    private class Config {
        private String UUID;
        private String IDENTITY;
        private boolean BEFORE = false;
        private String seqFormat;
    }

    private Config config = new Config();

    public void setUUID(String UUID) {
        config.UUID = UUID;
    }

    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            config.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            config.IDENTITY = IDENTITY;
        }
    }

    public void setSeqFormat(String seqFormat){
        config.seqFormat = seqFormat;
    }

    public void setBEFORE(String BEFORE) {
        config.BEFORE = "BEFORE".equalsIgnoreCase(BEFORE);
    }

    public String getUUID() {
        if (config.UUID != null && config.UUID.length() > 0) {
            return config.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    public String getIDENTITY() {
        if (config.IDENTITY != null && config.IDENTITY.length() > 0) {
            return config.IDENTITY;
        }
        //针对mysql的默认值
        return IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    public boolean getBEFORE() {
        return config.BEFORE;
    }

    public String getSeqFormat(){
        if (config.seqFormat != null && config.seqFormat.length() > 0) {
            return config.seqFormat;
        }
        return "{0}.nextval";
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
        for (Map.Entry<Class<?>, MapperTemplate> entry : registerMapper.entrySet()) {
            if (entry.getValue().supportMethod(msId)) {
                msIdSkip.put(msId, true);
                return true;
            }
        }
        msIdSkip.put(msId, false);
        return false;
    }

    /**
     * 获取MapperTemplate
     *
     * @param msId
     * @return
     */
    private MapperTemplate getMapperTemplate(String msId) {
        MapperTemplate mapperTemplate = null;
        if (msIdCache.get(msId) != null) {
            mapperTemplate = msIdCache.get(msId);
        } else {
            for (Map.Entry<Class<?>, MapperTemplate> entry : registerMapper.entrySet()) {
                if (entry.getValue().supportMethod(msId)) {
                    mapperTemplate = entry.getValue();
                    break;
                }
            }
            msIdCache.put(msId, mapperTemplate);
        }
        return mapperTemplate;
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     */
    public void setSqlSource(MappedStatement ms) {
        MapperTemplate mapperTemplate = getMapperTemplate(ms.getId());
        try {
            mapperTemplate.setSqlSource(ms);
        } catch (Exception e) {
            throw new RuntimeException("调用方法异常:" + e.getMessage());
        }
    }
}
