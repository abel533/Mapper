package tk.mybatis.mapper.common.pagable;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;

import java.util.List;

@RegisterMapper
public interface SelectPageMapper<T> extends
        SelectCountMapper<T>,
        SelectRowBoundsMapper<T> {

    default Page<T> selectByPage(T record, int pageNo, int pageSize){
        if(pageNo <= 0){
            pageNo = Page.DEFAULT_PAGE_NO;
        }
        if(pageSize <= 0 || pageSize > Page.DEFAULT_PAGE_NO){
            pageSize = Page.MAX_PAGE_SIZE;
        }
        int offset = (pageNo - 1) * pageSize;
        int limit = pageSize;
        int totalItems = selectCount(record);
        List<T> items = selectByRowBounds(record, new RowBounds(offset,limit));
        return new Page<>(items, totalItems, pageNo, pageSize);
    }
}
