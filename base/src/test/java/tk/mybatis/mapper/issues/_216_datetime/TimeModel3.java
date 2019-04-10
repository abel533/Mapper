package tk.mybatis.mapper.issues._216_datetime;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuzh
 */
@Table(name = "test_timestamp")
public class TimeModel3 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    @ColumnType(jdbcType = JdbcType.DATE)
    private Date    testDate;
    @ColumnType(jdbcType = JdbcType.TIME)
    private Date    testTime;
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    private Date    testDatetime;

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

    public Date getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(Date testDatetime) {
        this.testDatetime = testDatetime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }
}
