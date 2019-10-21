package tk.mybatis.mapper.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 创建时间
     */
//    @Column(insertable = false)
    private Date time;

    /**
     * 创建时间
     */
//    @Column(insertable = false)
    private Date ctime;

//    @Column(insertable = false)
    private Date ctimestamp;

    public Date getCtimestamp() {
        return ctimestamp;
    }

    public Person setCtimestamp(Date ctimestamp) {
        this.ctimestamp = ctimestamp;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Person setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Person setTime(Date time) {
        this.time = time;
        return this;
    }

    public Date getCtime() {
        return ctime;
    }

    public Person setCtime(Date ctime) {
        this.ctime = ctime;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", time=" + time +
                ", ctime=" + ctime +
                ", ctimestamp=" + ctimestamp +
                '}';
    }
}
