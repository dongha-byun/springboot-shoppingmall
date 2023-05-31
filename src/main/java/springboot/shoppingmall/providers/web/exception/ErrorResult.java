package springboot.shoppingmall.providers.web.exception;

import lombok.Data;

@Data
public class ErrorResult {
    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }
}
