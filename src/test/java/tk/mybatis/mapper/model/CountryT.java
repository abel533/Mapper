package tk.mybatis.mapper.model;

import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Description: Country
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:38)
 */
public class CountryT {
    @Id
    private Integer id;

    private String countryname;

    @Transient
    private String countrycode;

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
}
