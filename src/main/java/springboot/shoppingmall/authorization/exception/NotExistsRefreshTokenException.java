package springboot.shoppingmall.authorization.exception;

public class NotExistsRefreshTokenException extends RuntimeException{
    public NotExistsRefreshTokenException(String message) {
        super(message);
    }
}
