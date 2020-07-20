package tk.mybatis.mapper.additional.delete;

import tk.mybatis.mapper.common.base.BaseSelectMapper;

/**
 * @author jingkaihui
 * @date 2019/10/19
 */
public interface CourseMapper extends BaseSelectMapper<Course>, DeleteByPropertyMapper<Course> {
}
