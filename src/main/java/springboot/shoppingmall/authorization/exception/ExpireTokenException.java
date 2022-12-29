package springboot.shoppingmall.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ExpireTokenException extends RuntimeException{
    public ExpireTokenException() {
        super();
    }

    public ExpireTokenException(String message) {
        super(message);
    }

    public ExpireTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpireTokenException(Throwable cause) {
        super(cause);
    }

    protected ExpireTokenException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
