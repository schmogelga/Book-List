package misc.exception;

public class DataEntryException extends Exception {

    public DataEntryException(String message) {
        super(message);
    }

    public DataEntryException(Throwable cause) {
        super(cause);
    }

    public DataEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
