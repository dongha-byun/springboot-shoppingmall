package springboot.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.category.dto.CategoryResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private CategoryResponse category;
    private CategoryResponse subCategory;

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory()));
    }
}
