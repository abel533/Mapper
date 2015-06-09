package tk.mybatis.mapper.model;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liuzh on 2014/11/21.
 */
@Table(name = "USER_LOGIN")
public class UserLogin2 extends UserLogin2Key {
    private Date logindate;
    private String loginip;

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }
}
