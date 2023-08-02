package springboot.shoppingmall.common.validation.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BeanValidationErrorResponse {
    private List<BeanValidationError> errors = new ArrayList<>();

    public BeanValidationErrorResponse(Map<String, String> errors) {
        this.errors = errors.keySet().stream()
                .map(
                        key -> new BeanValidationError(key, errors.get(key))
                )
                .collect(Collectors.toList());
    }
}
