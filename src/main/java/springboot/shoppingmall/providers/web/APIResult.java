package springboot.shoppingmall.providers.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIResult<T> {
    private String message;
    private T data;
}
