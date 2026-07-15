package tk.mybatis.mapper.issues._949_nested_query;

import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * Mapper for issue 949 - nested query test
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * Query using a resultMap that has an association with a nested select.
     * With enableBaseResultMapFlag=true, this resultMap should NOT be replaced
     * even though the entity class (User) is a JPA entity.
     */
    List<User> selectUsersWithDetail();

    /**
     * Query using a resultMap that has ONLY an association (no explicit simple mappings).
     * This tests the edge case where resultMappings may be non-empty but only with association.
     */
    List<User> selectUsersWithDetailOnlyAssociation();

    /**
     * Query using a resultMap that has ONLY a discriminator (empty resultMappings).
     * Without the fix, setRawSqlSourceMapper would replace this resultMap with the JPA-generated
     * BaseMapperResultMap, losing the discriminator and breaking nested queries in case maps.
     * With the fix, this user-defined resultMap is preserved as-is.
     */
    List<User> selectUsersWithDiscriminator();

    /**
     * Nested select for UserDetail by userId.
     */
    UserDetail selectDetailByUserId(Integer userId);
}
