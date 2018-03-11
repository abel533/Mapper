package tk.mybatis.mapper.keysql;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liuzh
 */
@Table(name = "user")
public class UserSqlBefore {
    @Id
    @KeySql(sql = "select 12345", order = ORDER.BEFORE)
    private Integer id;
    private String name;

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
}
