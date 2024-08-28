package tk.mybatis.mapper.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于开窗函数 ROW_NUMBER 查询的查询参数对象
 *
 * @author lxh
 */
public class RowNumberExample {

    private static final boolean ASC = true;

    public final static RowNumberExample EMPTY = null;

    // 用于开窗函数 RowNumber 查询的分区列
    private final List<String> partCols;

    // 用于开窗函数 RowNumber 查询的排序列
    private final List<String> sortCols;

    // 用于开窗函数 RowNUmber 的排序方向（正序或逆序）
    private final boolean sortOriented;

    // 每组需要查询的排序编号，默认为 1
    private final int rank;

    public RowNumberExample(List<String> partCols,
                            List<String> sortCols,
                            boolean sortOriented,
                            int rank) {
        this.partCols = new ArrayList<String>(partCols);
        this.sortCols = new ArrayList<String>(sortCols);
        this.sortOriented = sortOriented;
        this.rank = rank;
    }

    public List<String> getPartCols() {
        return partCols;
    }

    public List<String> getSortCols() {
        return sortCols;
    }

    public boolean isSortOriented() {
        return sortOriented;
    }

    public int getRank() {
        return rank;
    }

    public static final class Builder {
        private final List<String> partCols = new ArrayList<String>();
        private final List<String> sortCols = new ArrayList<String>();
        private boolean sortOriented = ASC;
        private int rank = 1;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder partCols(List<String> partCols) {
            if (partCols == null || partCols.isEmpty()) {
                return this;
            }
            this.partCols.addAll(partCols);
            return this;
        }

        public Builder sortCols(List<String> sortCols) {
            if (sortCols == null || sortCols.isEmpty()) {
                return this;
            }
            this.sortCols.addAll(sortCols);
            return this;
        }

        public Builder sortOriented(boolean sortOriented) {
            this.sortOriented = sortOriented;
            return this;
        }

        public Builder rank(int rank) {
            this.rank = rank;
            return this;
        }

        public RowNumberExample build() {
            return new RowNumberExample(partCols, sortCols, sortOriented, rank);
        }
    }
}
