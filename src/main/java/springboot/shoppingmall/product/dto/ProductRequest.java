package springboot.shoppingmall.product.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private int price;
    private int count;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long subCategoryId;

    public static Product toProduct(ProductRequest productRequest, Category category, Category subCategory, Long partnerId){
        return new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getCount(), category, subCategory, partnerId);
    }
}
