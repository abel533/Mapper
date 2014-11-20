package com.github.abel533.mapper;

import org.apache.ibatis.type.JdbcType;

/**
 * ClassName:MybatisResult <br/>
 * Function: ADD FUNCTION. <br/>
 * Reason: ADD REASON. <br/>
 * Date: 2014年8月30日 下午2:31:09 <br/>
 *
 * @version 1.0
 * @see
 * @since JDK 1.6
 */
public class SimpleResultMap {

    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;

    /**
     * property.
     *
     * @return the property
     * @since JDK 1.6
     */
    public String getProperty() {
        return property;
    }

    /**
     * property.
     *
     * @param property the property to set
     * @since JDK 1.6
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * column.
     *
     * @return the column
     * @since JDK 1.6
     */
    public String getColumn() {
        return column;
    }

    /**
     * column.
     *
     * @param column the column to set
     * @since JDK 1.6
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * javaType.
     *
     * @return the javaType
     * @since JDK 1.6
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * javaType.
     *
     * @param javaType the javaType to set
     * @since JDK 1.6
     */
    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * jdbcType.
     *
     * @return the jdbcType
     * @since JDK 1.6
     */
    public JdbcType getJdbcType() {
        return jdbcType;
    }

    /**
     * jdbcType.
     *
     * @param jdbcType the jdbcType to set
     * @since JDK 1.6
     */
    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

}
