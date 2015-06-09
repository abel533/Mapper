package tk.mybatis.mapper.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by liuzh on 2014/11/21.
 */
public class UserInfoMap extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -7703830119762722918L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password;
    private String userType;
    private String realName;

    public Integer getId() {
        return (Integer) get("id");
    }

    public void setId(Integer id) {
        put("id", id);
    }

    public String getUserName() {
        return get("userName") != null ? (String) get("userName") : null;
    }

    public void setUserName(String userName) {
        put("userName", userName);
    }

    public String getPassword() {
        return get("password") != null ? (String) get("password") : null;
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getUserType() {
        return get("userType") != null ? (String) get("userType") : null;
    }

    public void setUserType(String userType) {
        put("userType", userType);
    }

    public String getRealName() {
        return get("realName") != null ? (String) get("realName") : null;
    }

    public void setRealName(String realName) {
        put("realName", realName);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserInfoMap{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(getUserName()).append('\'');
        sb.append(", password='").append(getPassword()).append('\'');
        sb.append(", userType='").append(getUserType()).append('\'');
        sb.append(", realName='").append(getRealName()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
