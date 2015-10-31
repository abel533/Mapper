package tk.mybatis.mapper.test.othres;

import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author liuzh
 * @since 2015-10-31 09:41
 */
public class StyleTest {
    private String[] fields = new String[]{
            "hello",
            "hello_world",
            //"hello_World",
            "helloWorld",
            "hello1",
            "hello_1"
    };

    @Test
    public void testNormal() {
        for (String field : fields) {
            Assert.assertEquals(field, StringUtil.convertByStyle(field, Style.normal));
        }
    }

    @Test
    public void testUppercase() {
        for (String field : fields) {
            Assert.assertEquals(field.toUpperCase(), StringUtil.convertByStyle(field, Style.uppercase));
        }
    }

    @Test
    public void testLowercase() {
        for (String field : fields) {
            Assert.assertEquals(field.toLowerCase(), StringUtil.convertByStyle(field, Style.lowercase));
        }
    }

    @Test
    public void testCamelhump() {
        for (String field : fields) {
            System.out.println(field + " - " + StringUtil.convertByStyle(field, Style.camelhump));
        }
    }

    @Test
    public void testCamelhumpUppercase() {
        for (String field : fields) {
            System.out.println(field + " - " + StringUtil.convertByStyle(field, Style.camelhumpAndUppercase));
        }
    }

    @Test
    public void testCamelhumpLowercase() {
        for (String field : fields) {
            System.out.println(field + " - " + StringUtil.convertByStyle(field, Style.camelhumpAndLowercase));
        }
    }

}
