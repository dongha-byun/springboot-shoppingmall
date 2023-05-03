package springboot.shoppingmall.order.exception;

public class OverQuantityException extends IllegalArgumentException{
    public OverQuantityException(String s) {
        super(s);
    }
}
