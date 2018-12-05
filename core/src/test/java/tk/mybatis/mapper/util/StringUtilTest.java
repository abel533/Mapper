package tk.mybatis.mapper.util;

import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.util.StringUtil;

public class StringUtilTest {

    @Test
    public void toUpperAsciiInputaOutputA() {
        final char c = 'a';
        final char retval = StringUtil.toUpperAscii(c);
        Assert.assertEquals('A', retval);
    }

    @Test
    public void toUpperAsciiInputNotNullOutputNotNull() {
        final char c = '\u0000';
        final char retval = StringUtil.toUpperAscii(c);
        Assert.assertEquals('\u0000', retval);
    }

    @Test
    public void isLowercaseAlphaInputyOutputTrue() {
        final char c = 'y';
        final boolean retval = StringUtil.isLowercaseAlpha(c);
        Assert.assertEquals(true, retval);
    }

    @Test
    public void isLowercaseAlphaInputNotNullOutputFalse() {
        final char c = '\u0000';
        final boolean retval = StringUtil.isLowercaseAlpha(c);
        Assert.assertEquals(false, retval);
    }

    @Test
    public void isLowercaseAlphaInputNotNullOutputFalse2() {
        final char c = '}';
        final boolean retval = StringUtil.isLowercaseAlpha(c);
        Assert.assertEquals(false, retval);
    }
}
