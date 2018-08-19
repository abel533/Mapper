package tk.mybatis.mapper.common.special;


import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.provider.SpecialProvider;

import java.util.List;

/**
 * 通用mapper借口，特殊方法，批量插入，支持批量插入的数据库都可以使用，例如mysql
 * @author wudy
 */
public interface InsertCustomKeyListMapp<T> {
    /**
     * 批量插入，主键自定义（无需自增），支持批量插入数据的数据库使用，如mysql
     * @param recordList
     * @return
     */
    @InsertProvider(type = SpecialProvider.class,method = "dynamicSQL")
    int insertCustomKeyList(List<T> recordList);
}
