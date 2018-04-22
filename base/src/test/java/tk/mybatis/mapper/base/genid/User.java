package tk.mybatis.mapper.base.genid;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

/**
 * @author liuzh
 */
public class User {
    @Id
    @KeySql(genId = UUIdGenId.class)
    private String id;
    private String name;
    private String code;

    public User() {
    }

    public User(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
