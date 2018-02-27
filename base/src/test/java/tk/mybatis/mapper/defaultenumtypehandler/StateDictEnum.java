package tk.mybatis.mapper.defaultenumtypehandler;

/**
 * @author liuzh
 */
public enum StateDictEnum implements Dict {
    enabled(1),
    disabled(2);

    private int value;

    private StateDictEnum(int value){
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

}
