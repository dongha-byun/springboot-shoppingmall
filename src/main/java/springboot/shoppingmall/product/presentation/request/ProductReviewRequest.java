package springboot.shoppingmall.product.presentation.request;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductReviewCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest implements Serializable {

    @NotBlank(message = "review content can not be null")
    private String content;

    @Max(value = 5)
    @Min(value = 0)
    private int score;

    public ProductReviewCreateDto toDto() {
        return new ProductReviewCreateDto(
                content, score
        );
    }
}