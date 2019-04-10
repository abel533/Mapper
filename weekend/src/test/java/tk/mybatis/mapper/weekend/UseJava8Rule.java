package tk.mybatis.mapper.weekend;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author liuzh
 */
public class UseJava8Rule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String version = System.getProperty("java.version");
                if (!version.contains("1.6.") && !version.contains("1.7.")) {
                    base.evaluate();
                }
            }
        };
    }
}
