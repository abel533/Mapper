package tk.mybatis.mapper.generatedvalue;

import javax.persistence.*;

/**
 * @author liuzh
 */
@Table(name = "user_auto_increment")
public class UserSqlAfter {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "SELECT LAST_INSERT_ID()")
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
