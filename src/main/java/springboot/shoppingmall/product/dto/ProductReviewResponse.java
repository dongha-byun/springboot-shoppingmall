package springboot.shoppingmall.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductReview;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private Long id;
    private String content;
    private LocalDateTime writeDate;
    private String userName;

    public static ProductReviewResponse of(ProductReview review) {
        // return new ProductReviewResponse(review.getId(), review.getContent(), review.getWriteDate(), review.getWriteName());
        return new ProductReviewResponse(review.getId(), review.getContent(), review.getWriteDate(), null);
    }
}
