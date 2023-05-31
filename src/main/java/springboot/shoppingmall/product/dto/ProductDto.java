package springboot.shoppingmall.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;

@NoArgsConstructor
@Data
public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private int count;
    private String detail;
    private Long categoryId;
    private Long subCategoryId;
    private Long partnerId;
    private String storedThumbnailName;
    private String viewThumbnailName;

    public ProductDto(String name, int price, int count, String detail,
                      Long categoryId, Long subCategoryId, Long partnerId,
                      String storedThumbnailName, String viewThumbnailName) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.detail = detail;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.partnerId = partnerId;
        this.storedThumbnailName = storedThumbnailName;
        this.viewThumbnailName = viewThumbnailName;
    }

    @QueryProjection
    public ProductDto(Long id, String name, int price, int count, String detail, Long categoryId, Long subCategoryId,
                      Long partnerId, String storedThumbnailName, String viewThumbnailName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.detail = detail;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.partnerId = partnerId;
        this.storedThumbnailName = storedThumbnailName;
        this.viewThumbnailName = viewThumbnailName;
    }
}
