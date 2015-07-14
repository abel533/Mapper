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

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理主要逻辑，最关键的一个类
 * <p/>
 * <p>项目地址 : <a href="https://github.com/abel533/Mapper" target="_blank">https://github.com/abel533/Mapper</a></p>
 *
 * @author liuzh
 */
public class MapperHelper {
    /**
     * 缓存skip结果
     */
    private final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();
    /**
     * 注册的通用Mapper接口
     */
    private Map<Class<?>, MapperTemplate> registerMapper = new ConcurrentHashMap<Class<?>, MapperTemplate>();
    /**
     * 缓存msid和MapperTemplate
     */
    private Map<String, MapperTemplate> msIdCache = new HashMap<String, MapperTemplate>();
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
     * 对于一般的getAllIfColumnNode，是否判断!=''，默认不判断
     */
    private boolean notEmpty = false;
    private Config config = new Config();

    /**
     * 默认构造方法
     */
    public MapperHelper() {
        initSpringVersion();
    }

    /**
     * 带配置的构造方法
     *
     * @param properties
     */
    public MapperHelper(Properties properties) {
        this();
        setProperties(properties);
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
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
    public String getSpringVersion() {
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
            templateClass = EmptyMapperProvider.class;
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
        if (!registerMapper.containsKey(mapperClass)) {
            registerMapper.put(mapperClass, fromMapperClass(mapperClass));
        }
        //自动注册继承的接口
        Class<?>[] interfaces = mapperClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                registerMapper(anInterface);
            }
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
     * 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
     *
     * @param order
     */
    public void setOrder(String order) {
        config.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    /**
     * 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
     *
     * @param catalog
     */
    public void setCatalog(String catalog) {
        config.catalog = catalog;
    }

    /**
     * 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
     * <br>如果同时设置了catalog,优先使用catalog.tablename
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
     * 设置UUID生成策略
     * <br>配置UUID生成策略需要使用OGNL表达式
     * <br>默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
     *
     * @param UUID
     */
    public void setUUID(String UUID) {
        config.UUID = UUID;
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
     * 主键自增回写方法,默认值MYSQL,详细说明请看文档
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
     * 序列的获取规则,使用{num}格式化参数，默认值为{0}.nextval，针对Oracle
     * <br>可选参数一共3个，对应0,1,2,分别为SequenceName，ColumnName, PropertyName
     *
     * @param seqFormat
     */
    public void setSeqFormat(String seqFormat) {
        config.seqFormat = seqFormat;
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
        String notEmpty = properties.getProperty("notEmpty");
        if (notEmpty != null && notEmpty.length() > 0) {
            this.notEmpty = notEmpty.equalsIgnoreCase("TRUE");
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
     * 配置完成后，执行下面的操作
     * <br>处理configuration中全部的MappedStatement
     *
     * @param configuration
     */
    public void processConfiguration(Configuration configuration) {
        Collection<MappedStatement> collection = configuration.getMappedStatements();
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

        public String getIdentityRetrievalStatement() {
            return identityRetrievalStatement;
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
}