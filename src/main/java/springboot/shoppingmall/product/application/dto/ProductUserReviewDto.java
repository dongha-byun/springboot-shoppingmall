package springboot.shoppingmall.product.application.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.ThumbnailInfo;
import springboot.shoppingmall.product.domain.ProductReview;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductUserReviewDto {
    private Long id;
    private String content;
    private int score;
    private String productName;
    private LocalDateTime writeDate;
    private List<ThumbnailInfo> images;

    public static ProductUserReviewDto of(ProductReview entity) {
        List<ThumbnailInfo> images = new ArrayList<>();
        if(entity.getImages() != null) {
            images = entity.getImages().stream()
                    .map(image -> new ThumbnailInfo(image.getStoredFileName(), image.getViewFileName()))
                    .collect(Collectors.toList());
        }
        return new ProductUserReviewDto(
                entity.getId(), entity.getContent(), entity.getScore(),
                entity.getProductName(), entity.getWriteDate(),
                images
        );
    }
}
