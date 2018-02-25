package tk.mybatis.mapper.issues._216_datetime;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author liuzh
 */
@Table(name = "test_timestamp")
public class TimeModel2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer   id;
    private Date      testDate;
    private Date      testTime;
    private Timestamp testDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Timestamp getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(Timestamp testDatetime) {
        this.testDatetime = testDatetime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }
}
