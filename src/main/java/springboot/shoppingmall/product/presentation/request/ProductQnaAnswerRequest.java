package springboot.shoppingmall.product.presentation.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProductQnaAnswerRequest {

    @NotBlank(message = "답변내용은 공백일 수 없습니다.")
    private String content;
}
