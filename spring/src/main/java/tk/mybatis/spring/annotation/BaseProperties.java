package tk.mybatis.spring.annotation;

/**
 * @author liuzh
 */
public class BaseProperties {
    public static final String MYBATIS_PREFIX = "mybatis";

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    private String[] basePackages;

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
