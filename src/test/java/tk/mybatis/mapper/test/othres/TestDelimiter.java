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
