package tk.mybatis.mapper.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.typehandler.StringType2Handler;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Description: Country
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:38)
 */
public class Country implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = -1626761012846137805L;

    @Id
    @OrderBy("desc")
    private Integer id;

    @Column
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = StringType2Handler.class)
    private String countryname;
    private String countrycode;

    @Transient
    private String dynamicTableName123;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", countryname='" + countryname + '\'' +
                ", countrycode='" + countrycode + '\'' +
                '}';
    }

    @Override
    public String getDynamicTableName() {
        return dynamicTableName123;
    }

    @Override
    public void setDynamicTableName(String dynamicTableName) {
        this.dynamicTableName123 = dynamicTableName;
    }
}
