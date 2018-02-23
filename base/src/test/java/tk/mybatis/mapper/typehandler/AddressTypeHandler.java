package tk.mybatis.mapper.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author liuzh
 */
public class AddressTypeHandler extends BaseTypeHandler<Address> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Address parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    private Address convertToAddress(String addressStr){
        if(addressStr == null || addressStr.length() == 0){
            return null;
        }
        String[] strings = addressStr.split("/");
        Address address = new Address();
        if(strings.length > 0 && strings[0].length() > 0){
            address.setProvince(strings[0]);
        }
        if(strings.length > 1 && strings[1].length() > 0){
            address.setCity(strings[1]);
        }
        return address;
    }

    @Override
    public Address getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToAddress(rs.getString(columnName));
    }

    @Override
    public Address getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToAddress(rs.getString(columnIndex));
    }

    @Override
    public Address getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToAddress(cs.getString(columnIndex));
    }
}