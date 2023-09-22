package springboot.shoppingmall.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewCreateDto {
    private String content;
    private int score;
}
