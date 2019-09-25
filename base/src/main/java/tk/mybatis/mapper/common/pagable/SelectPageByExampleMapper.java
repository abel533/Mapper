package tk.mybatis.mapper.common.pagable;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;

import java.util.List;

@RegisterMapper
public interface SelectPageByExampleMapper<T> extends
        SelectCountByExampleMapper<T>,
        SelectByExampleRowBoundsMapper<T> {

    default Page<T> selectPageByExample(Object example, int pageNo, int pageSize){
        if(pageNo <= 0){
            pageNo = Page.DEFAULT_PAGE_NO;
        }
        if(pageSize <= 0 || pageSize > Page.DEFAULT_PAGE_NO){
            pageSize = Page.MAX_PAGE_SIZE;
        }
        int offset = (pageNo - 1) * pageSize;
        int limit = pageSize;
        int totalItems = selectCountByExample(example);
        List<T> items = selectByExampleAndRowBounds(example, new RowBounds(offset,limit));
        return new Page<>(items, totalItems, pageNo, pageSize);
    }
}
