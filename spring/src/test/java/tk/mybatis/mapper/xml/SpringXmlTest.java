package tk.mybatis.mapper.xml;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author liuzh
 */
public class SpringXmlTest {

    private ClassPathXmlApplicationContext context;

    @Test
    public void testCountryMapper() {
        context = new ClassPathXmlApplicationContext("tk/mybatis/mapper/xml/spring.xml");
        CountryMapper countryMapper = context.getBean(CountryMapper.class);

        List<Country> countries = countryMapper.selectAll();
        Assert.assertNotNull(countries);
        Assert.assertEquals(183, countries.size());
    }


}
