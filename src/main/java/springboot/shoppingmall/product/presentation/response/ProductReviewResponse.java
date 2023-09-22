package springboot.shoppingmall.product.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;
import springboot.shoppingmall.utils.DateUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private Long id;
    private String content;
    private int score;
    private String writeDate;

    public static ProductReviewResponse of(ProductReview review) {
        return new ProductReviewResponse(review.getId(), review.getContent(), review.getScore(),
                DateUtils.toStringOfLocalDateTIme(review.getWriteDate()));
    }

    public static ProductReviewResponse of(ProductReviewDto dto) {
        return new ProductReviewResponse(dto.getId(), dto.getContent(), dto.getScore(),
                DateUtils.toStringOfLocalDateTIme(dto.getWriteDate()));
    }
}
