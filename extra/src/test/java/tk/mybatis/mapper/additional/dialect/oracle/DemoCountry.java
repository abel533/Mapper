package tk.mybatis.mapper.additional.dialect.oracle;

import tk.mybatis.mapper.additional.insertlist.UUIdGenId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

/**
 * @description:
 * @author: qrqhuangcy
 * @date: 2018-11-17
 **/
public class DemoCountry {

    @Id
    @KeySql(genId = UUIdGenId.class)
    private String countryId;

    private String countryName;

    private String countryCode;

    public DemoCountry(String countryId, String countryName, String countryCode) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
