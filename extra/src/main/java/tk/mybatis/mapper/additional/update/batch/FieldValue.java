package tk.mybatis.mapper.additional.update.batch;

import tk.mybatis.mapper.weekend.Fn;

public class FieldValue<T> {

    /**
     * 字段名
     * 形式必须是 类::方法
     * e.g. Country::getCountryname
     */
    private Fn<T, ?> fn;

    /**
     * 更新的字段值
     */
    private Object value;

    public FieldValue(Fn<T, ?> fn, Object value) {
        this.fn = fn;
        this.value = value;
    }

    public FieldValue() {
    }

    public Fn<T, ?> getFn() {
        return fn;
    }

    public void setFn(Fn<T, ?> fn) {
        this.fn = fn;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
