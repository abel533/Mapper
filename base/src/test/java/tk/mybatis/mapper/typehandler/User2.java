package tk.mybatis.mapper.typehandler;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author liuzh
 */
@Table(name = "user")
public class User2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer   id;
    private String    name;
    @Column
    private Address   address;
    private StateEnum state;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }
}
