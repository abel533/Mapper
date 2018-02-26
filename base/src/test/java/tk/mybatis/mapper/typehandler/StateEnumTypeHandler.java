package tk.mybatis.mapper.typehandler;

import org.apache.ibatis.type.EnumOrdinalTypeHandler;

public class StateEnumTypeHandler extends EnumOrdinalTypeHandler<StateEnum> {
    public StateEnumTypeHandler(Class<StateEnum> type) {
        super(type);
    }
}