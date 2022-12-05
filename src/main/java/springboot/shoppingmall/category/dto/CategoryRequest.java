package springboot.shoppingmall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.domain.Category;

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
