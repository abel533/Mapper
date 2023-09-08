package tk.mybatis.mapper.additional.delete;

import tk.mybatis.mapper.annotation.LogicDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.time.LocalDate;

/**
 * @author jingkaihui
 * @date 2019/10/19
 */
public class Course {

    @Id
    private Integer id;

    private String name;

    private Integer price;

    private LocalDate published;

    @LogicDelete
    @Column(name = "is_deleted")
    private Boolean isDeleted;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
