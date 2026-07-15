package tk.mybatis.mapper.issues._949_nested_query;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity for issue 949 - nested query test
 */
@Table(name = "user_detail949")
public class UserDetail {

    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDetail{id=" + id + ", userId=" + userId + ", email='" + email + "'}";
    }
}
