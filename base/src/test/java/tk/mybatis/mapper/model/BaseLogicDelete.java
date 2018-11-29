package tk.mybatis.mapper.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import tk.mybatis.mapper.annotation.LogicDelete;

public class BaseLogicDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @LogicDelete(isDeletedValue = 0, notDeletedValue = 1)
    @Column(name = "is_valid")
    protected Integer isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
