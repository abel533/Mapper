package com.github.abel533.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableHelper {

    public static final String SIMPLE = "-Simple";

    private static List<String> queryList;

    /**
     * 存放基于@Table注解动态构建出来的MappedStatement（相当于XML）
     */
    private static Map<String, SimpleTableMap> tableMap = new HashMap<String, SimpleTableMap>();

    private static List<String> msidList = new ArrayList<String>();


    /**
     * initMappedStatement:缓存通用接口MappedStatement对象，配合过滤器使用
     *
     * @since JDK 1.6
     */
    private static void initMappedStatement() {
        if (tableMap.isEmpty()) {
            return;
        }
        SqlSession session = null;
        try {
            session = applicationContext.getBean(SqlSessionFactoryBean.class)
                    .getObject().openSession();
            Configuration configuration = session.getConfiguration();
            List<String> list = ReflectionUtils
                    .findMethodAllName(SimpleMapper.class);
            MappedStatement ms = null;
            for (String string : list) {
                String name = string.substring(string.lastIndexOf(".") + 1,
                        string.length());
                if (isLoad(name)) {
                    ms = configuration.getMappedStatement(string);
                    for (Map.Entry<String, SimpleTableMap> entry : tableMap
                            .entrySet()) {
                        // 由于所有的查询的SQL语句不提供动态SQL所以可以缓存MappedStatement对象，如果sql是动态的MappedStatement需要根据参数不同每次生成新的MappedStatement对象
                        Builder builder = new Builder(
                                ms.getConfiguration(), ms.getId() + "-"
                                + entry.getKey(), ms.getSqlSource(),
                                ms.getSqlCommandType());
                        ResultMap.Builder simpleResultMapBuilder = new ResultMap.Builder(
                                ms.getConfiguration(), builder.id(),
                                Class.forName(entry.getKey()),
                                new ArrayList<ResultMapping>());
                        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
                        builder.resultMaps(resultMaps);
                        builder.resultSetType(ms.getResultSetType());
                        builder.cache(ms.getCache());
                        builder.flushCacheRequired(ms.isFlushCacheRequired());
                        builder.useCache(ms.isUseCache());
                        resultMaps.add(simpleResultMapBuilder.build());
                        builder.resultMaps(resultMaps);
                        if (null == entry.getValue().getMappedStatementMap()) {
                            entry.getValue().setMappedStatementMap(
                                    new HashMap<String, MappedStatement>());
                        }
                        configuration.addMappedStatement(builder.build());
                        msidList.add(ms.getId() + "-" + entry.getKey());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }

    }

    private static boolean isLoad(String name) {
        if (null == queryList || queryList.isEmpty()) {
            return false;
        }

        for (String string : queryList) {
            if (name.startsWith(string)) {
                return true;
            }
        }

        return false;

    }

    public static void init() {
        initTable();
        initMappedStatement();
    }

    /**
     * initTable:缓存所有表结构实体，避免每次调用反射
     *
     * @param map
     * @since JDK 1.6
     */
    private static void initTable() {
        Map<String, Object> map = applicationContext
                .getBeansWithAnnotation(Table.class);
        SimpleResultMap srm = null;
        List<SimpleResultMap> simpleResultMap = null;
        Map<String, String> columnsMap = null;
        StringBuilder sb = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            SimpleTableMap stm = new SimpleTableMap();
            stm.setPrimaryKey(ReflectionUtils.findPrimary(entry.getValue()));
            stm.setSequenceName(ReflectionUtils.findSequenceName(entry.getValue()));
            stm.setTableName(ReflectionUtils.findTable(entry.getValue()));
            stm.setKeyProperty(ReflectionUtils.findPrimaryProperty(entry.getValue()));
            simpleResultMap = new ArrayList<SimpleResultMap>();
            columnsMap = ReflectionUtils.findColumns(entry.getValue());
            sb = new StringBuilder(stm.getPrimaryKey()).append(",");
            for (Map.Entry<String, String> column : columnsMap.entrySet()) {
                srm = new SimpleResultMap();
                srm.setColumn(column.getKey());
                srm.setProperty(column.getValue());
                srm.setJavaType(ReflectionUtils.findType(entry.getValue(),column.getValue()));
                simpleResultMap.add(srm);
                sb.append(column.getKey()).append(" as ").append(column.getValue()).append(",");
            }
            stm.setSimpleResultMap(simpleResultMap);
            stm.setSelectColumns(sb.deleteCharAt(sb.length() - 1).toString());

            tableMap.put(entry.getValue().getClass().getName(), stm);
        }
    }

    /**
     * msidList.
     *
     * @return the msidList
     * @since JDK 1.6
     */
    public static List<String> getMsidList() {
        return msidList;
    }

    /**
     * tableMap.
     *
     * @return the tableMap
     * @since JDK 1.6
     */
    public static Map<String, SimpleTableMap> getTableMap() {
        return tableMap;
    }

    /**
     * queryList.
     *
     * @param queryList the queryList to set
     * @since JDK 1.6
     */
    public static void setQueryList(List<String> queryList) {
        TableHelper.queryList = queryList;
    }

}
