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

    @NotBlank(message = "리뷰 내용은 필수항목 입니다.")
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
