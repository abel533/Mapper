package tk.mybatis.mapper.version;

import tk.mybatis.mapper.annotation.Version;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

/**
 * @author liuzh
 */
@Table(name = "user_timestamp")
public class UserTimestamp {
    @Id
    private Integer id;

    @Version
    private Timestamp joinDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }
}
