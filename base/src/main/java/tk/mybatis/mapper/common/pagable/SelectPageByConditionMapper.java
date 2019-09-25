package tk.mybatis.mapper.common.pagable;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;

import java.util.List;

@RegisterMapper
public interface SelectPageByConditionMapper<T> extends
        SelectCountByConditionMapper<T>,
        SelectByConditionRowBoundsMapper<T> {

    default Page<T> selectPageByCondition(Object condition, int pageNo, int pageSize){
        if(pageNo <= 0){
            pageNo = Page.DEFAULT_PAGE_NO;
        }
        if(pageSize <= 0 || pageSize > Page.DEFAULT_PAGE_NO){
            pageSize = Page.MAX_PAGE_SIZE;
        }
        int offset = (pageNo - 1) * pageSize;
        int limit = pageSize;
        int totalItems = selectCountByCondition(condition);
        List<T> items = selectByConditionAndRowBounds(condition, new RowBounds(offset,limit));
        return new Page<>(items, totalItems, pageNo, pageSize);
    }
}
