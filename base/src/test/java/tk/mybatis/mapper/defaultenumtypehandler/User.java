package tk.mybatis.mapper.defaultenumtypehandler;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author liuzh
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer       id;
    private String        name;
    private LockDictEnum  lock;
    private StateDictEnum state;

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

    public LockDictEnum getLock() {
        return lock;
    }

    public void setLock(LockDictEnum lock) {
        this.lock = lock;
    }

    public StateDictEnum getState() {
        return state;
    }

    public void setState(StateDictEnum state) {
        this.state = state;
    }
}
