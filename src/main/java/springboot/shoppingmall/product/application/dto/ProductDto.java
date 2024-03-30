package springboot.shoppingmall.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.dto.CategoryDto;
import springboot.shoppingmall.product.domain.Product;

@NoArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String productCode;
    private String name;
    private int price;
    private int stock;
    private String detail;
    private Long categoryId;
    private Long subCategoryId;
    private Long partnersId;
    private String storedThumbnailName;
    private String viewThumbnailName;

    @Builder
    @QueryProjection
    public ProductDto(Long id, String productCode, String name, int price, int stock, String detail,
                      Long categoryId, Long subCategoryId, Long partnerId,
                      String storedThumbnailName, String viewThumbnailName) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.partnersId = partnerId;
        this.storedThumbnailName = storedThumbnailName;
        this.viewThumbnailName = viewThumbnailName;
    }

    public static ProductDto of(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .productCode(entity.getProductCode())
                .name(entity.getName())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .detail(entity.getDetail())
                .categoryId(entity.getCategoryId())
                .subCategoryId(entity.getSubCategoryId())
                .partnerId(entity.getPartnerId())
                .storedThumbnailName(entity.getThumbnail())
                .build();
    }
}
