package com.github.abel533.mapper;

import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;
import java.util.Map;

/**
 * ClassName:TableMap <br/>
 * Function: ADD FUNCTION. <br/>
 * Reason: ADD REASON. <br/>
 * Date: 2014年8月30日 下午2:44:00 <br/>
 *
 * @version 1.0
 * @see
 * @since JDK 1.6
 */
public class SimpleTableMap {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     * 主键属性
     */
    private String keyProperty;

    /**
     * 查询SQL例如：ID,USER_NAME,PASSWORD
     */
    private String selectColumns;

    /**
     * 序列名称
     */
    private String sequenceName;

    /**
     * 动态构建出来的MappedStatement（相当于XML）
     */
    private Map<String, MappedStatement> mappedStatementMap;

    /**
     * 所有列名-属性名-JAVA类型-数据库类型关系
     */
    private List<SimpleResultMap> simpleResultMap;

    /**
     * tableName.
     *
     * @return the tableName
     * @since JDK 1.6
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * tableName.
     *
     * @param tableName the tableName to set
     * @since JDK 1.6
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * primaryKey.
     *
     * @return the primaryKey
     * @since JDK 1.6
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * primaryKey.
     *
     * @param primaryKey the primaryKey to set
     * @since JDK 1.6
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * selectColumns.
     *
     * @return the selectColumns
     * @since JDK 1.6
     */
    public String getSelectColumns() {
        return selectColumns;
    }

    /**
     * selectColumns.
     *
     * @param selectColumns the selectColumns to set
     * @since JDK 1.6
     */
    public void setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
    }

    /**
     * sequenceName.
     *
     * @return the sequenceName
     * @since JDK 1.6
     */
    public String getSequenceName() {
        return sequenceName;
    }

    /**
     * sequenceName.
     *
     * @param sequenceName the sequenceName to set
     * @since JDK 1.6
     */
    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    /**
     * mappedStatementMap.
     *
     * @return the mappedStatementMap
     * @since JDK 1.6
     */
    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    /**
     * mappedStatementMap.
     *
     * @param mappedStatementMap the mappedStatementMap to set
     * @since JDK 1.6
     */
    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }

    /**
     * simpleResultMap.
     *
     * @return the simpleResultMap
     * @since JDK 1.6
     */
    public List<SimpleResultMap> getSimpleResultMap() {
        return simpleResultMap;
    }

    /**
     * simpleResultMap.
     *
     * @param simpleResultMap the simpleResultMap to set
     * @since JDK 1.6
     */
    public void setSimpleResultMap(List<SimpleResultMap> simpleResultMap) {
        this.simpleResultMap = simpleResultMap;
    }

    /**
     * keyProperty.
     *
     * @return the keyProperty
     * @since JDK 1.6
     */
    public String getKeyProperty() {
        return keyProperty;
    }

    /**
     * keyProperty.
     *
     * @param keyProperty the keyProperty to set
     * @since JDK 1.6
     */
    public void setKeyProperty(String keyProperty) {
        this.keyProperty = keyProperty;
    }

}
