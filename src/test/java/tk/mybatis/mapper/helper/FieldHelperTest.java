package tk.mybatis.mapper.helper;

import org.junit.Test;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.FieldHelper;
import tk.mybatis.mapper.model.Country;

import javax.persistence.Id;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author liuzh_3nofxnp
 * @since 2015-12-06 18:47
 */
public class FieldHelperTest {

    @Test
    public void test1() throws IntrospectionException {
        List<EntityField> fields = FieldHelper.getFields(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");

        fields = FieldHelper.getAll(Country.class);
        for (EntityField field : fields) {
            System.out.println(field.getName() + "  -  @Id:" + field.isAnnotationPresent(Id.class) + "  -  javaType:" + field.getJavaType());
        }
        System.out.println("======================================");
    }
}
