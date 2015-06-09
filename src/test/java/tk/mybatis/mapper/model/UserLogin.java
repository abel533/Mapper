package tk.mybatis.mapper.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by liuzh on 2014/11/21.
 */
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logid;

    @Id
    private String username;
    private Date logindate;
    private String loginip;

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

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "logid=" + logid +
                ", username='" + username + '\'' +
                ", logindate=" + logindate +
                ", loginip='" + loginip + '\'' +
                '}';
    }
}
