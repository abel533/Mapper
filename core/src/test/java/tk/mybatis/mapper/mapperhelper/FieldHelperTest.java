package tk.mybatis.mapper.mapperhelper;

import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.entity.EntityField;

import java.util.List;

/**
 * @author liuzh
 */
public class FieldHelperTest {

    static class User {
        private Integer id;
        private String name;
        private transient String other1;
        public static final Integer FINAL = 1;
    }

    @Test
    public void testUser(){
        List<EntityField> fieldList = FieldHelper.getFields(User.class);
        Assert.assertEquals(2, fieldList.size());
        Assert.assertEquals("id", fieldList.get(0).getName());
        Assert.assertEquals("name", fieldList.get(1).getName());
    }

    static class Admin {
        private Integer admin;
        private User user;
    }

    @Test
    public void testComplex(){
        List<EntityField> fieldList = FieldHelper.getFields(Admin.class);
        Assert.assertEquals(2, fieldList.size());
        Assert.assertEquals("admin", fieldList.get(0).getName());
        Assert.assertEquals("user", fieldList.get(1).getName());
    }

}
