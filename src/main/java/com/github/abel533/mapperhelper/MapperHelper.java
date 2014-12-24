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

package com.github.abel533.mapperhelper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 处理主要逻辑，最关键的一个类
 *
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
     * 缓存已经处理过的Collection<MappedStatement>
     */
    private Set<Collection<MappedStatement>> collectionSet = new HashSet<Collection<MappedStatement>>();

    /**
     * 是否使用的Spring
     */
    private boolean spring = false;

    /**
     * 是否为Spring4.x以上版本
     */
    private boolean spring4 = false;

    /**
     * Spring版本号
     */
    private String springVersion;

    /**
     * 默认构造方法
     */
    public MapperHelper() {
    }

    /**
     * 带配置的构造方法
     *
     * @param properties
     */
    public MapperHelper(Properties properties) {
        setProperties(properties);
    }

    /**
     * 缓存初始化时的SqlSession
     */
    private List<SqlSession> sqlSessions = new ArrayList<SqlSession>();

    /**
     * 针对Spring注入需要处理的SqlSession
     *
     * @param sqlSessions
     */
    public void setSqlSessions(SqlSession[] sqlSessions) {
        if (sqlSessions != null && sqlSessions.length > 0) {
            this.sqlSessions.addAll(Arrays.asList(sqlSessions));
        }
    }

    /**
     * Spring初始化方法，使用Spring时需要配置init-method="initMapper"
     */
    public void initMapper() {
        //只有Spring会执行这个方法,所以Spring配置的时候,从这儿可以尝试获取Spring的版本
        //先判断Spring版本,对下面的操作有影响
        //Spring4以上支持泛型注入,因此可以扫描通用Mapper
        initSpringVersion();
        for (SqlSession sqlSession : sqlSessions) {
            processConfiguration(sqlSession.getConfiguration());
        }
    }

    /**
     * 检测Spring版本号,Spring4.x以上支持泛型注入
     */
    private void initSpringVersion() {
        try {
            //反射获取SpringVersion
            Class<?> springVersionClass = Class.forName("org.springframework.core.SpringVersion");
            springVersion = (String) springVersionClass.getDeclaredMethod("getVersion", new Class<?>[0]).invoke(null, new Object[0]);
            spring = true;
            if (springVersion.indexOf(".") > 0) {
                int MajorVersion = Integer.parseInt(springVersion.substring(0, springVersion.indexOf(".")));
                if (MajorVersion > 3) {
                    spring4 = true;
                } else {
                    spring4 = false;
                }
            }
        } catch (Exception e) {
            spring = false;
            spring4 = false;
        }
    }

    /**
     * 是否为Spring4.x以上版本
     *
     * @return
     */
    public boolean isSpring4() {
        return spring4;
    }

    /**
     * 是否为Spring4.x以上版本
     *
     * @return
     */
    public boolean isSpring() {
        return spring;
    }

    /**
     * 获取Spring版本号
     *
     * @return
     */
    public String getSpringVersion(){
        return springVersion;
    }

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
            } else if (method.isAnnotationPresent(InsertProvider.class)) {
                InsertProvider provider = method.getAnnotation(InsertProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(DeleteProvider.class)) {
                DeleteProvider provider = method.getAnnotation(DeleteProvider.class);
                tempClass = provider.type();
                methodSet.add(method.getName());
            } else if (method.isAnnotationPresent(UpdateProvider.class)) {
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
    public void registerMapper(Class<?> mapperClass) {
        if (registerMapper.get(mapperClass) == null) {
            registerMapper.put(mapperClass, fromMapperClass(mapperClass));
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
            throw new RuntimeException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
        }
    }

    /**
     * 方便Spring注入
     *
     * @param mappers
     */
    public void setMappers(String[] mappers) {
        if (mappers != null && mappers.length > 0) {
            for (String mapper : mappers) {
                registerMapper(mapper);
            }
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
        private String catalog;
        private String schema;
    }

    private Config config = new Config();

    /**
     * 设置UUID
     *
     * @param UUID
     */
    public void setUUID(String UUID) {
        config.UUID = UUID;
    }

    /**
     * 设置主键自增回写方法，默认MYSQL
     *
     * @param IDENTITY
     */
    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            config.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            config.IDENTITY = IDENTITY;
        }
    }

    /**
     * 设置selectKey方法的ORDER，默认AFTER
     *
     * @param order
     */
    public void setOrder(String order) {
        config.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    /**
     * 设置序列格式化，默认值"{0}.nextval"
     *
     * @param seqFormat
     */
    public void setSeqFormat(String seqFormat) {
        config.seqFormat = seqFormat;
    }

    /**
     * 设置catalog，默认""
     *
     * @param catalog
     */
    public void setCatalog(String catalog) {
        config.catalog = catalog;
    }

    /**
     * 设置schema，默认""
     *
     * @param schema
     */
    public void setSchema(String schema) {
        config.schema = schema;
    }

    /**
     * 获取表前缀，带catalog或schema
     *
     * @return
     */
    public String getPrefix() {
        if (config.catalog != null && config.catalog.length() > 0) {
            return config.catalog;
        }
        if (config.schema != null && config.schema.length() > 0) {
            return config.catalog;
        }
        return "";
    }

    /**
     * 获取UUID生成规则
     *
     * @return
     */
    public String getUUID() {
        if (config.UUID != null && config.UUID.length() > 0) {
            return config.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    /**
     * 获取主键自增回写SQL
     *
     * @return
     */
    public String getIDENTITY() {
        if (config.IDENTITY != null && config.IDENTITY.length() > 0) {
            return config.IDENTITY;
        }
        //针对mysql的默认值
        return IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    /**
     * 获取SelectKey的Order
     *
     * @return
     */
    public boolean getBEFORE() {
        return config.BEFORE;
    }

    /**
     * 获取序列格式化模板
     *
     * @return
     */
    public String getSeqFormat() {
        if (config.seqFormat != null && config.seqFormat.length() > 0) {
            return config.seqFormat;
        }
        return "{0}.nextval";
    }

    /**
     * 获取表名
     *
     * @param entityClass
     * @return
     */
    public String getTableName(Class<?> entityClass) {
        EntityHelper.EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (prefix.equals("")) {
            //使用全局配置
            prefix = getPrefix();
        }
        if (!prefix.equals("")) {
            return prefix + "." + entityTable.getName();
        }
        return entityTable.getName();
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
            if (mapperTemplate != null) {
                mapperTemplate.setSqlSource(ms);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用方法异常:" + e.getMessage());
        }
    }

    /**
     * 配置属性
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        String UUID = properties.getProperty("UUID");
        if (UUID != null && UUID.length() > 0) {
            setUUID(UUID);
        }
        String IDENTITY = properties.getProperty("IDENTITY");
        if (IDENTITY != null && IDENTITY.length() > 0) {
            setIDENTITY(IDENTITY);
        }
        String seqFormat = properties.getProperty("seqFormat");
        if (seqFormat != null && seqFormat.length() > 0) {
            setSeqFormat(seqFormat);
        }
        String catalog = properties.getProperty("catalog");
        if (catalog != null && catalog.length() > 0) {
            setCatalog(catalog);
        }
        String schema = properties.getProperty("schema");
        if (schema != null && schema.length() > 0) {
            setSchema(schema);
        }
        String ORDER = properties.getProperty("ORDER");
        if (ORDER != null && ORDER.length() > 0) {
            setOrder(ORDER);
        }
        //注册通用接口
        String mapper = properties.getProperty("mappers");
        if (mapper != null && mapper.length() > 0) {
            String[] mappers = mapper.split(",");
            for (String mapperClass : mappers) {
                if (mapperClass.length() > 0) {
                    registerMapper(mapperClass);
                }
            }
        }
    }

    /**
     * 处理configuration中全部的MappedStatement
     *
     * @param configuration
     */
    public void processConfiguration(Configuration configuration) {
        Collection<MappedStatement> collection = configuration.getMappedStatements();
        //防止反复处理一个
        if (collectionSet.contains(collection)) {
            return;
        } else {
            collectionSet.add(collection);
        }
        int size = collection.size();
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                if (isMapperMethod(ms.getId())) {
                    if (ms.getSqlSource() instanceof ProviderSqlSource) {
                        setSqlSource(ms);
                    }
                }
            }
            //处理过程中可能会新增selectKey，导致ms增多，所以这里判断大小，重新循环
            if (collection.size() != size) {
                size = collection.size();
                iterator = collection.iterator();
            }
        }
    }
}