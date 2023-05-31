package springboot.shoppingmall.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ExpireTokenException extends RuntimeException{
    public ExpireTokenException(String message) {
        super(message);
    }
}
