package tk.mybatis.mapper.gensql;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;

/**
 * 生成 SQL，初始化时执行
 *
 * @author liuzh
 */
public interface GenSql {

    class NULL implements GenSql {
        @Override
        public String genSql(EntityTable entityTable, EntityColumn entityColumn) {
            throw new UnsupportedOperationException();
        }
    }

    String genSql(EntityTable entityTable, EntityColumn entityColumn);

}
