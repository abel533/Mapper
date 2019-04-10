package tk.mybatis.mapper.version;

import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author liuzh
 */
@Table(name = "user_int")
public class UserInt {
    @Id
    private Integer id;

    @Version
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
