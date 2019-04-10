/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    public Integer getId() {
        return (Integer) get("id");
    }

    public void setId(Integer id) {
        put("id", id);
    }

    public String getPassword() {
        return get("password") != null ? (String) get("password") : null;
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getRealName() {
        return get("realName") != null ? (String) get("realName") : null;
    }

    public void setRealName(String realName) {
        put("realName", realName);
    }

    public String getUserName() {
        return get("userName") != null ? (String) get("userName") : null;
    }

    public void setUserName(String userName) {
        put("userName", userName);
    }

    public String getUserType() {
        return get("userType") != null ? (String) get("userType") : null;
    }

    public void setUserType(String userType) {
        put("userType", userType);
    }
}
