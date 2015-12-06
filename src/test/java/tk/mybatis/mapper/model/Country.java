package tk.mybatis.mapper.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.typehandler.StringType2Handler;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Description: Country
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:38)
 */
public class Country extends Entity<Integer, String> implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = -1626761012846137805L;

    @Column
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = StringType2Handler.class)
    private String countryname;
    private String countrycode;

    @Transient
    private String dynamicTableName123;

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
    public String getDynamicTableName() {
        return dynamicTableName123;
    }

    public void setDynamicTableName123(String dynamicTableName) {
        this.dynamicTableName123 = dynamicTableName;
    }
}
