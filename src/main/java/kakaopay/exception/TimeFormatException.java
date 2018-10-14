package kakaopay.exception;

public class TimeFormatException  extends RestException{
    public TimeFormatException(String message) {
        super(message);
    }

    public TimeFormatException(String field, String message) {
        super(field, message);
    }
}
