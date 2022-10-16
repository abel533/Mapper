package tk.mybatis.mapper.mapperhelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;

import javax.persistence.*;

public class SqlHelperTest {

    private Config config;

    @Before
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);
        EntityHelper.initEntityNameMap(User.class, config);
    }

    @Test
    public void testLogicDeleteSql() {
        String wherePKColumns = SqlHelper.wherePKColumns(User.class);
        Assert.assertEquals("<where> AND id = #{id} AND is_valid = 1</where>", wherePKColumns);

        String whereAllIfColumns = SqlHelper.whereAllIfColumns(User.class, false);
        Assert.assertEquals("<where><if test=\"id != null\"> AND id = #{id}</if><if test=\"username != null\"> AND username = #{username}</if> AND is_valid = 1</where>", whereAllIfColumns);

        String isLogicDeletedColumn = SqlHelper.whereLogicDelete(User.class, true);
        Assert.assertEquals(" AND is_valid = 0", isLogicDeletedColumn);

        String notLogicDeletedColumn = SqlHelper.whereLogicDelete(User.class, false);
        Assert.assertEquals(" AND is_valid = 1", notLogicDeletedColumn);

        String updateSetColumns = SqlHelper.updateSetColumns(User.class, null, false, false);
        Assert.assertEquals("<set>username = #{username},is_valid = 1,</set>", updateSetColumns);
    }

}

@Table(name = "tb_user")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @LogicDelete(isDeletedValue = 0, notDeletedValue = 1)
    @Column(name = "is_valid")
    private Integer isValid;

    @Override
    public String toString() {
        return "TbUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
