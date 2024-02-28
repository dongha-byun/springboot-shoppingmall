package springboot.shoppingmall.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.product.application.ThumbnailInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductCreateDto {
    private String name;
    private int price;
    private int stock;
    private String detail;
    private Long categoryId;
    private Long subCategoryId;
    private ThumbnailInfo thumbnailInfo;
}
