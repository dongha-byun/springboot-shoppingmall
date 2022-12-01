package springboot.shoppingmall.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.domain.product.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private Long parentId;

    public static Category toCategory(CategoryRequest categoryRequest, Category parent) {
        return new Category(categoryRequest.getName(), parent);
    }
}
