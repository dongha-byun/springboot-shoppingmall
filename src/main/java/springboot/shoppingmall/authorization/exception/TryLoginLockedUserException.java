package springboot.shoppingmall.authorization.exception;

public class TryLoginLockedUserException extends IllegalStateException{
    public TryLoginLockedUserException(String s) {
        super(s);
    }
}
