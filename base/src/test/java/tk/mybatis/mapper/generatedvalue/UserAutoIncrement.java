package tk.mybatis.mapper.generatedvalue;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liuzh
 */
@Table(name = "user_auto_increment")
public class UserAutoIncrement {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(insertable = false)
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
