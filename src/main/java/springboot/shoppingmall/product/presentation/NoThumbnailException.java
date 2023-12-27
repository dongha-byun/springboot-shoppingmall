package springboot.shoppingmall.product.presentation;

public class NoThumbnailException extends RuntimeException{
    public NoThumbnailException(String message) {
        super(message);
    }
}
