package tk.mybatis.mapper.issues._949_nested_query;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity for issue 949 - nested query test
 */
@Table(name = "user949")
public class User {

    @Id
    private Integer id;

    private String name;

    private UserDetail detail;

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

    public UserDetail getDetail() {
        return detail;
    }

    public void setDetail(UserDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', detail=" + detail + "}";
    }
}
