package tk.mybatis.mapper.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by liuzh on 2014/11/21.
 */
public class UserLogin2Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logid;

    @Id
    private String username;

    public Integer getLogid() {
        return logid;
    }

    public void setLogid(Integer logid) {
        this.logid = logid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserLogin2Key{" +
                "logid=" + logid +
                ", username='" + username + '\'' +
                '}';
    }
}
