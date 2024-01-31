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
    private CategoryDto category;
    private CategoryDto subCategory;
    private Long partnersId;
    private String storedThumbnailName;
    private String viewThumbnailName;

    public ProductDto(String name, String productCode, int price, int stock, String detail,
                      CategoryDto category, CategoryDto subCategory, Long partnersId,
                      String storedThumbnailName, String viewThumbnailName) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.category = category;
        this.subCategory = subCategory;
        this.partnersId = partnersId;
        this.storedThumbnailName = storedThumbnailName;
        this.viewThumbnailName = viewThumbnailName;
    }

    @Builder
    @QueryProjection
    public ProductDto(Long id, String productCode, String name, int price, int stock, String detail,
                      CategoryDto category, CategoryDto subCategory, Long partnerId,
                      String storedThumbnailName, String viewThumbnailName) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.category = category;
        this.subCategory = subCategory;
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
                .category(CategoryDto.of(entity.getCategory()))
                .subCategory(CategoryDto.of(entity.getSubCategory()))
                .partnerId(entity.getPartnerId())
                .storedThumbnailName(entity.getThumbnail())
                .build();
    }
}
