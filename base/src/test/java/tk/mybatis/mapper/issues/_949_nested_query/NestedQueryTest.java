package tk.mybatis.mapper.issues._949_nested_query;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

/**
 * Test for issue #949: nested queries fail when enableBaseResultMapFlag is true.
 *
 * The root cause: setRawSqlSourceMapper was replacing ANY user-defined resultMap whose
 * resultMappings list was empty (e.g. a resultMap with only a discriminator, or only
 * nested-select associations whose column is shared). The fix is to only replace
 * auto-generated inline resultMaps (ID ends with "-Inline", created from resultType=),
 * never user-configured resultMaps (created from resultMap=).
 */
public class NestedQueryTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnableBaseResultMapFlag(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = NestedQueryTest.class.getResource("mybatis-config-issue949.xml");
        return toReader(url);
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = NestedQueryTest.class.getResource("CreateDB.sql");
        return toReader(url);
    }

    /**
     * A resultMap with explicit simple column mappings AND an association with nested select.
     * resultMappings is NOT empty, so setRawSqlSourceMapper must not replace it.
     * Verifies that the nested select (detail) is still executed.
     */
    @Test
    public void testNestedQueryWithExplicitMappings() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = mapper.selectUsersWithDetail();

            Assert.assertNotNull("Users should not be null", users);
            Assert.assertEquals("Should have 2 users", 2, users.size());

            for (User user : users) {
                Assert.assertNotNull("User id should not be null", user.getId());
                Assert.assertNotNull("User name should not be null", user.getName());
                Assert.assertNotNull("User detail should not be null - nested select must have been executed",
                        user.getDetail());
                Assert.assertNotNull("UserDetail email should not be null",
                        user.getDetail().getEmail());
            }
        } finally {
            sqlSession.close();
        }
    }

    /**
     * A resultMap with ONLY an association (no explicit simple column mappings).
     * resultMappings is NOT empty (contains the association), so setRawSqlSourceMapper
     * must not replace it. The nested select (detail) must still be executed.
     * Note: id is null because MyBatis auto-mapping skips "id" once it's consumed as
     * the association column — that is expected behaviour for this resultMap configuration.
     */
    @Test
    public void testNestedQueryWithOnlyAssociation() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = mapper.selectUsersWithDetailOnlyAssociation();

            Assert.assertNotNull("Users should not be null", users);
            Assert.assertEquals("Should have 2 users", 2, users.size());

            for (User user : users) {
                // The nested select (detail) should still be executed.
                Assert.assertNotNull("User detail should not be null - nested select must have been executed",
                        user.getDetail());
                Assert.assertNotNull("UserDetail email should not be null",
                        user.getDetail().getEmail());
            }
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Core bug scenario for issue #949.
     *
     * A user-defined resultMap with ONLY a discriminator has empty resultMappings.
     * Before the fix, setRawSqlSourceMapper replaced it with the JPA-generated
     * BaseMapperResultMap, wiping out the discriminator and breaking nested queries
     * that lived inside the discriminator-case result maps.
     *
     * After the fix (only replace inline "-Inline" resultMaps), the discriminator
     * result map is preserved:
     *   - user id=1 is routed to discriminatorCase1Map → nested select loads detail
     *   - user id=2 is routed to discriminatorCase2Map → no detail
     */
    @Test
    public void testNestedQueryWithDiscriminator() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = mapper.selectUsersWithDiscriminator();

            Assert.assertNotNull("Users should not be null", users);
            Assert.assertEquals("Should have 2 users", 2, users.size());

            // Find user1 and user2
            User user1 = users.stream().filter(u -> u.getId() != null && u.getId() == 1).findFirst().orElse(null);
            User user2 = users.stream().filter(u -> u.getId() != null && u.getId() == 2).findFirst().orElse(null);

            Assert.assertNotNull("user1 should be found", user1);
            Assert.assertNotNull("user2 should be found", user2);

            // user1 goes through discriminatorCase1Map which has a nested select for detail
            Assert.assertNotNull(
                    "user1 detail should be loaded by nested select via discriminator case 1 - " +
                    "this fails without the fix because the discriminator resultMap was replaced",
                    user1.getDetail());
            Assert.assertEquals("user1@example.com", user1.getDetail().getEmail());

            // user2 goes through discriminatorCase2Map which has no nested select
            Assert.assertNull("user2 detail should be null (no nested select in case 2)", user2.getDetail());
        } finally {
            sqlSession.close();
        }
    }
}
