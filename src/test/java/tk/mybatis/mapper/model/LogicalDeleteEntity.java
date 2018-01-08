package tk.mybatis.mapper.model;

import tk.mybatis.mapper.annotation.LogicalDelete;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author CarlJia.
 * @date 2018-01-08.
 */
public class LogicalDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @LogicalDelete
    private String delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "LogicalDeleteEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
