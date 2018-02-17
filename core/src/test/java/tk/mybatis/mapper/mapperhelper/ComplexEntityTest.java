package tk.mybatis.mapper.mapperhelper;

import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityTable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author liuzh
 */
public class ComplexEntityTest {

    private Config config;

    private Configuration configuration;

    @Before
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.camelhump);

        configuration = new Configuration();
    }

    static class Address {
        private String street;
        private String zipCode;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

    static enum State {
        ENABLE,
        DISABLE
    }

    public static class AddressHandler implements TypeHandler<Address> {
        public AddressHandler() {
            System.out.println("init");
        }

        @Override
        public void setParameter(PreparedStatement ps, int i, Address parameter, JdbcType jdbcType) throws SQLException {
            ps.setString(i, parameter.getStreet());
        }

        @Override
        public Address getResult(ResultSet rs, String columnName) throws SQLException {
            final String value = rs.getString(columnName);
            final Address address = new Address();
            address.setStreet(value);
            return address;
        }

        @Override
        public Address getResult(ResultSet rs, int columnIndex) throws SQLException {
            final String value = rs.getString(columnIndex);
            final Address address = new Address();
            address.setStreet(value);
            return address;
        }

        @Override
        public Address getResult(CallableStatement cs, int columnIndex) throws SQLException {
            return null;
        }
    }

    static class User {
        @Id
        private Long id;
        private String userName;

        @Column
        @ColumnType(typeHandler = AddressHandler.class)
        private Address address;
        @Column
        private State state;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
    }

    @Test
    public void test() {
        Class<?> entityClass = User.class;
        EntityHelper.initEntityNameMap(entityClass, config);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SqlHelper.selectAllColumns(entityClass));
        final EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        sqlBuilder.append(SqlHelper.fromTable(entityClass, entityTable.getName()));
        sqlBuilder.append(SqlHelper.whereAllIfColumns(entityClass, config.isNotEmpty()));
        final String sql = sqlBuilder.toString();
        Assert.assertEquals("SELECT id,user_name,address,state  FROM user " +
                "<where>" +
                "<if test=\"id != null\"> AND id = #{id}</if>" +
                "<if test=\"userName != null\"> AND user_name = #{userName}</if>" +
                "<if test=\"address != null\"> AND address = #{address, typeHandler=tk.mybatis.mapper.mapperhelper.ComplexEntityTest.AddressHandler}</if>" +
                "<if test=\"state != null\"> AND state = #{state}</if></where>", sql);

        final ResultMap resultMap = entityTable.getResultMap(configuration);
        final List<ResultMapping> resultMappings = resultMap.getResultMappings();
        final ResultMapping idMapping = resultMappings.get(0);
        final ResultMapping userNameMapping = resultMappings.get(1);
        final ResultMapping addressMapping = resultMappings.get(2);
        final ResultMapping stateMapping = resultMappings.get(3);

        Assert.assertEquals("id", idMapping.getColumn());
        Assert.assertEquals("id", idMapping.getProperty());
        Assert.assertTrue(idMapping.getFlags().contains(ResultFlag.ID));

        Assert.assertEquals("user_name", userNameMapping.getColumn());
        Assert.assertEquals("userName", userNameMapping.getProperty());

        Assert.assertEquals("address", addressMapping.getColumn());
        Assert.assertEquals("address", addressMapping.getProperty());
        Assert.assertEquals(AddressHandler.class, addressMapping.getTypeHandler().getClass());

        Assert.assertEquals("state", stateMapping.getColumn());
        Assert.assertEquals("state", stateMapping.getProperty());
        Assert.assertEquals(EnumTypeHandler.class, stateMapping.getTypeHandler().getClass());


    }

}
