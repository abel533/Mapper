package tk.mybatis.mapper.mapper;

import org.apache.ibatis.session.SqlSession;

/**
 * Description: MybatisHelper
 * Author: liuzh
 * Update: liuzh(2014-06-06 13:33)
 */
public class MybatisHelper {
    /**
     * 获取Session
     * @return
     */
    public static SqlSession getSqlSession(){
        return MybatisJavaHelper.getSqlSession();
    }
}
