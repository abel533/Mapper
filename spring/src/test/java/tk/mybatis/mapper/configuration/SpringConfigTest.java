package tk.mybatis.mapper.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.List;

/**
 * @author liuzh
 */
public class SpringConfigTest {

    private ClassPathXmlApplicationContext context;

    @Test
    public void testCountryMapper() {
        context = new ClassPathXmlApplicationContext("tk/mybatis/mapper/configuration/spring.xml");
        CountryMapper countryMapper = context.getBean(CountryMapper.class);

        List<Country> countries = countryMapper.selectAll();
        Assert.assertNotNull(countries);
        Assert.assertEquals(183, countries.size());
    }


}
