package tk.mybatis.mapper.defaultenumtypehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author liuzh
 */
public class DictTypeHandler extends BaseTypeHandler<Dict> {

    private final Class<? extends Dict> type;
    private final Dict[]                enums;

    public DictTypeHandler(Class<? extends Dict> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Dict parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    private Dict convertToDict(int value) {
        for (Dict anEnum : enums) {
            if (anEnum.getValue() == value) {
                return anEnum;
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName() + " by value.");
    }

    @Override
    public Dict getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return convertToDict(i);
    }

    @Override
    public Dict getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return convertToDict(i);
    }

    @Override
    public Dict getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return convertToDict(i);
    }
}