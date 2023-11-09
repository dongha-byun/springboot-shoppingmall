package springboot.shoppingmall.partners.presentation.exception;

public class NotEqualPasswordException extends IllegalArgumentException{
    public NotEqualPasswordException() {
        super();
    }

    public NotEqualPasswordException(String s) {
        super(s);
    }

    public NotEqualPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEqualPasswordException(Throwable cause) {
        super(cause);
    }
}
