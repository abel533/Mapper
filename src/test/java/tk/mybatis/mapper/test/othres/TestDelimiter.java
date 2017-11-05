/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
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

package tk.mybatis.mapper.test.othres;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuzh
 * @since 2017/7/13.
 */
public class TestDelimiter {

    public static final Pattern DELIMITER = Pattern.compile("^[`\\[\"]?(.*?)[`\\]\"]?$");

    @Test
    public void test(){
        Matcher matcher = DELIMITER.matcher("normal");
        if(matcher.find()){
            Assert.assertEquals("normal", matcher.group(1));
        }

        matcher = DELIMITER.matcher("`mysql`");
        if(matcher.find()){
            Assert.assertEquals("mysql", matcher.group(1));
        }

        matcher = DELIMITER.matcher("[sqlserver]");
        if(matcher.find()){
            Assert.assertEquals("sqlserver", matcher.group(1));
        }

        matcher = DELIMITER.matcher("\"oracle\"");
        if(matcher.find()){
            Assert.assertEquals("oracle", matcher.group(1));
        }
    }
}
