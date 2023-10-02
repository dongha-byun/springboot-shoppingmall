package springboot.shoppingmall.providers.presentation.exception;

import lombok.Data;

@Data
public class ErrorResult {
    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }
}
