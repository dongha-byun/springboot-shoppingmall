package springboot.shoppingmall.common.validation.bean;

import static java.util.stream.Collectors.*;

import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BeanValidationException extends RuntimeException{
    private final BindingResult bindingResult;
    public BeanValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public Map<String, String> getErrors() {
        return bindingResult.getFieldErrors()
                .stream()
                .filter(fieldError -> StringUtils.hasText(fieldError.getDefaultMessage()))
                .collect(toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
    }
}
