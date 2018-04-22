package tk.mybatis.mapper.base.genid;

import tk.mybatis.mapper.genid.GenId;

/**
 * 一个简单的实现，不考虑任何特殊情况，不要用于生产环境
 *
 * @author liuzh
 */
public class SimpleGenId implements GenId<Long> {
    private Long    time;
    private Integer seq;

    @Override
    public synchronized Long genId(String table, String column) {
        long current = System.currentTimeMillis();
        if (time == null || time != current) {
            time = current;
            seq = 1;
        } else if (current == time) {
            seq++;
        }
        return (time << 20) | seq;
    }
}
