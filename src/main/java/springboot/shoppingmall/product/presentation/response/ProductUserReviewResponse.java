package springboot.shoppingmall.product.presentation.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductReview;
import springboot.shoppingmall.product.application.ThumbnailInfo;
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
    private List<ThumbnailInfo> images;

    public static ProductUserReviewResponse of(ProductReview review) {
        List<ThumbnailInfo> images = new ArrayList<>();
        if(review.getImages() != null) {
            images = review.getImages().stream()
                    .map(image -> new ThumbnailInfo(image.getStoredFileName(), image.getViewFileName()))
                    .collect(Collectors.toList());
        }
        return new ProductUserReviewResponse(review.getId(), review.getContent(), review.getScore(),
                review.getProductName(),
                DateUtils.toStringOfLocalDateTIme(review.getWriteDate()),
                images
        );
    }
}