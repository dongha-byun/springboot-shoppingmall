package springboot.shoppingmall.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.ThumbnailInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCreateDto {
    private String name;
    private int price;
    private int quantity;
    private String detail;
    private Long categoryId;
    private Long subCategoryId;
    private ThumbnailInfo thumbnailInfo;
}
