package springboot.shoppingmall.product.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;

@Data
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private int price;
    private int count;
    @NotBlank
    private Long categoryId;
    @NotBlank
    private Long subCategoryId;

    public ProductRequest(String name, int price, int count, Long categoryId, Long subCategoryId) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
    }

    public static Product toProduct(ProductRequest productRequest, Category category, Category subCategory){
        return new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getCount(), category, subCategory);
    }
}
