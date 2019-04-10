package tk.mybatis.mapper.base.genid;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * @author liuzh
 */
public class UUIdGenId implements GenId<String> {
    @Override
    public String genId(String table, String column) {
        return UUID.randomUUID().toString();
    }
}
