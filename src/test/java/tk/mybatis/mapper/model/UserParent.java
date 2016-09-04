package tk.mybatis.mapper.model;

import javax.persistence.Column;

/**
 * @author liuzh_3nofxnp
 * @since 2016-08-29 22:36
 */
public class UserParent {

    @Column(updatable = false)
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
