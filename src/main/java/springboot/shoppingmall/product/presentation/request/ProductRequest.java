package springboot.shoppingmall.product.presentation.request;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.application.ThumbnailInfo;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable{

    @NotBlank(message = "상품 명은 필수항목 입니다.")
    private String name;
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    private int price;
    @Min(value = 1, message = "재고는 1개 이상이어야 합니다.")
    private int count;
    @NotNull(message = "상위 카테고리를 선택하세요.")
    private Long categoryId;
    @NotNull(message = "하위 카테고리를 선택하세요.")
    private Long subCategoryId;
    private String detail;

    public ProductCreateDto toDto(ThumbnailInfo thumbnailInfo) {
        return ProductCreateDto.builder()
                .name(this.name)
                .price(this.price)
                .quantity(this.count)
                .categoryId(this.categoryId)
                .subCategoryId(this.subCategoryId)
                .detail(this.detail)
                .thumbnailInfo(thumbnailInfo)
                .build();
    }
}
