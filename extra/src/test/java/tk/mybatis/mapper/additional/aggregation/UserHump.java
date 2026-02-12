package tk.mybatis.mapper.additional.aggregation;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.io.Serializable;

@Table(name = "user_hump")
public class UserHump implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String userName;
    private String userRole;
    //存储聚合函数值
    @Transient
    private Long aggregation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getAggregation() {
        return aggregation;
    }

    public void setAggregation(Long aggregation) {
        this.aggregation = aggregation;
    }
}
