package springboot.shoppingmall.admin.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class APIResult<T> {
    private String message;
    private T data;
}
