package springboot.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.utils.DateUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUserReviewResponse {
    private Long id;
    private String content;
    private int score;
    private String productName;
    private String writeDate;

    public static ProductUserReviewResponse of(ProductReview review) {
        return new ProductUserReviewResponse(review.getId(), review.getContent(), review.getScore(),
                review.getProductName(),
                DateUtils.toStringOfLocalDateTIme(review.getWriteDate()));
    }
}
