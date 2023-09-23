package springboot.shoppingmall.product.presentation.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductUserReviewDto;
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

    public static ProductUserReviewResponse of(ProductUserReviewDto dto) {
        return new ProductUserReviewResponse(
                dto.getId(), dto.getContent(), dto.getScore(),
                dto.getProductName(), DateUtils.toStringOfLocalDateTIme(dto.getWriteDate()),
                dto.getImages()
        );
    }
}
