package springboot.shoppingmall.product.presentation.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.application.ThumbnailInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable{

    private String name;
    private int price;
    private int count;
    @NotNull
    private Long categoryId;
    @NotNull
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
