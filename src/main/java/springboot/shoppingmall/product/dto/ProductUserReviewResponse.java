package springboot.shoppingmall.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductReview;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUserReviewResponse {
    private Long id;
    private String content;
    private String productName;
    private LocalDateTime writeDate;

    public static ProductUserReviewResponse of(ProductReview productReview) {
        return new ProductUserReviewResponse(productReview.getId(), productReview.getContent(), productReview.getProductName(), productReview.getWriteDate());
    }
}
