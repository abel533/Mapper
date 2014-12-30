package test.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`user login info`")
public class UserLoginInfo {
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 登录名
     */
    @Id
    private String username;

    /**
     * 登录时间
     */
    private Date logindate;

    /**
     * 登录IP
     */
    private String loginip;

    /**
     * @return Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取登录名
     *
     * @return username - 登录名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置登录名
     *
     * @param username 登录名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取登录时间
     *
     * @return logindate - 登录时间
     */
    public Date getLogindate() {
        return logindate;
    }

    /**
     * 设置登录时间
     *
     * @param logindate 登录时间
     */
    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

    /**
     * 获取登录IP
     *
     * @return loginip - 登录IP
     */
    public String getLoginip() {
        return loginip;
    }

    /**
     * 设置登录IP
     *
     * @param loginip 登录IP
     */
    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }
}