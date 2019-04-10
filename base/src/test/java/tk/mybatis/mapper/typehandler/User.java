package tk.mybatis.mapper.typehandler;

import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author liuzh
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    private String name;
    @ColumnType(typeHandler = AddressTypeHandler.class)
    private Address address;
    //@ColumnType(typeHandler = StateEnumTypeHandler.class)
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
