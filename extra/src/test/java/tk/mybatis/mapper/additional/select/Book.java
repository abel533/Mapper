package tk.mybatis.mapper.additional.select;

import jakarta.persistence.Id;
import java.time.LocalDate;

/**
 * @author jingkaihui
 * @date 2019/10/19
 */
public class Book {

    @Id
    private Integer id;

    private String name;

    private Integer price;

    private LocalDate published;

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
}
