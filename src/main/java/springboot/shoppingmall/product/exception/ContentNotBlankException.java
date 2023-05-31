package springboot.shoppingmall.product.exception;

public class ContentNotBlankException extends IllegalArgumentException{
    public ContentNotBlankException() {
        super();
    }

    public ContentNotBlankException(String s) {
        super(s);
    }

    public ContentNotBlankException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNotBlankException(Throwable cause) {
        super(cause);
    }
}
