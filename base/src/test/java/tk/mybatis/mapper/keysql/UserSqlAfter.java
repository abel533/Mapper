package tk.mybatis.mapper.keysql;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author liuzh
 */
@Table(name = "user_auto_increment")
public class UserSqlAfter {
    @Id
    @KeySql(sql = "SELECT LAST_INSERT_ID()", order = ORDER.AFTER)
    @Column(insertable = false)
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
