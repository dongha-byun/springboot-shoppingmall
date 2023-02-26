package springboot.shoppingmall.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {

    @NotBlank(message = "review content can not be null")
    private String content;
    private int score;
}
