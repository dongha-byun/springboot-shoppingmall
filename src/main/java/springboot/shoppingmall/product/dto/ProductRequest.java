package springboot.shoppingmall.product.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static ProductDto toDto(ProductRequest productRequest, Long partnerId
            , String storedThumbnailName, String viewThumbnailName) {
        return new ProductDto(productRequest.getName(), productRequest.getPrice(), productRequest.getCount()
                , productRequest.getDetail() , productRequest.getCategoryId(), productRequest.getSubCategoryId()
                , partnerId, storedThumbnailName, viewThumbnailName);
    }
}
