package tk.mybatis.mapper.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Description: Country
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:38)
 */
public class Country implements Serializable {
    private static final long serialVersionUID = -1626761012846137805L;
    @Id
    private Integer id;
    @Column
    private String countryname;
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
