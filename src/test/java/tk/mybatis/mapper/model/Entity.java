package tk.mybatis.mapper.model;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author liuzh_3nofxnp
 * @since 2015-12-06 10:31
 */
public class Entity<ID extends Serializable, NAME extends Serializable> {

    private ID id;

    @Transient
    private NAME name;

    @Id
    public ID getId() {
        return id;
    }

    @OrderBy("desc")
    public void setId(ID id) {
        this.id = id;
    }

    public NAME getName() {
        return name;
    }

    public void setName(NAME name) {
        this.name = name;
    }
}