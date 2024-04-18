package tk.mybatis.mapper.rawresultmap;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author liuzh
 */
@NameStyle(Style.camelhump)
@Table(name = "user")
public class User {

    @Id
    private Integer id;

    private String name;

    @Column(name = "user_name")
    private String uname;

    @Column(name = "age__int__aa")
    private Integer age;

    private Date createTime;

    @Transient
    private String email;

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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uname='" + uname + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                ", email='" + email + '\'' +
                '}';
    }
}
