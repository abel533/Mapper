package tk.mybatis.mapper;

public class LogicDeleteException extends RuntimeException {

    public LogicDeleteException() {
        super();
    }

    public LogicDeleteException(String message) {
        super(message);
    }

    public LogicDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicDeleteException(Throwable cause) {
        super(cause);
    }
}
