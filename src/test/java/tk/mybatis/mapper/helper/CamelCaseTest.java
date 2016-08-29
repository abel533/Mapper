package tk.mybatis.mapper.helper;

import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author liuzh_3nofxnp
 * @since 2016-08-29 22:02
 */
public class CamelCaseTest {

    @Test
    public void testCamelhumpToUnderline() {
        Assert.assertEquals("user_id", StringUtil.camelhumpToUnderline("userId"));
        Assert.assertEquals("sys_user", StringUtil.camelhumpToUnderline("sysUser"));
        Assert.assertEquals("sys_user_role", StringUtil.camelhumpToUnderline("sysUserRole"));
        Assert.assertEquals("s_function", StringUtil.camelhumpToUnderline("sFunction"));
    }
}
