package springboot.shoppingmall.common.validation.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BeanValidationError {
    private String field;
    private String message;
}
