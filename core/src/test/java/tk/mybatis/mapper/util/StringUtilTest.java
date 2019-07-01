/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.util;

import org.junit.Test;
import org.junit.Assert;
import tk.mybatis.mapper.code.Style;

public class StringUtilTest {

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(StringUtil.isEmpty(null));
        Assert.assertTrue(StringUtil.isEmpty(""));

        Assert.assertFalse(StringUtil.isEmpty(" "));
        Assert.assertFalse(StringUtil.isEmpty("foo"));
    }

    @Test
    public void testIsNotEmpty() {
        Assert.assertFalse(StringUtil.isNotEmpty(null));
        Assert.assertFalse(StringUtil.isNotEmpty(""));

        Assert.assertTrue(StringUtil.isNotEmpty(" "));
        Assert.assertTrue(StringUtil.isNotEmpty("foo"));
    }

    @Test
    public void testConvertByStyle() {
        Assert.assertEquals("fOo",
                StringUtil.convertByStyle("fOo", Style.normal));
        Assert.assertEquals("f_oo",
                StringUtil.convertByStyle("fOo", Style.camelhump));
        Assert.assertEquals("FOO",
                StringUtil.convertByStyle("fOo", Style.uppercase));
        Assert.assertEquals("foo",
                StringUtil.convertByStyle("FoO", Style.lowercase));
        Assert.assertEquals("fo_o",
                StringUtil.convertByStyle("FoO", Style.camelhumpAndLowercase));
        Assert.assertEquals("F_OO",
                StringUtil.convertByStyle("fOo", Style.camelhumpAndUppercase));
    }

    @Test
    public void testCamelhumpToUnderline() {
        Assert.assertEquals("foo", StringUtil.camelhumpToUnderline("foo"));
        Assert.assertEquals("f_oo", StringUtil.camelhumpToUnderline("fOo"));
    }

    @Test
    public void testUnderlineToCamelhump() {
        Assert.assertEquals("foo", StringUtil.underlineToCamelhump("foo"));
        Assert.assertEquals("foo", StringUtil.underlineToCamelhump("Foo"));
    }

    @Test
    public void testIsUppercaseAlpha() {
        Assert.assertTrue(StringUtil.isUppercaseAlpha('F'));

        Assert.assertFalse(StringUtil.isUppercaseAlpha('f'));
    }

    @Test
    public void testIsLowercaseAlpha() {
        Assert.assertTrue(StringUtil.isLowercaseAlpha('f'));

        Assert.assertFalse(StringUtil.isLowercaseAlpha('F'));
    }

    @Test
    public void testToUpperAscii() {
        Assert.assertEquals('F', StringUtil.toUpperAscii('f'));
        Assert.assertEquals('F', StringUtil.toUpperAscii('F'));
    }

    @Test
    public void testToLowerAscii() {
        Assert.assertEquals('f', StringUtil.toLowerAscii('f'));
        Assert.assertEquals('f', StringUtil.toLowerAscii('F'));
    }
}
