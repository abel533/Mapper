package tk.mybatis.mapper.generatedvalue;

import javax.persistence.*;

/**
 * @author liuzh
 */
@Table(name = "user_auto_increment")
public class UserAutoIncrementIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
