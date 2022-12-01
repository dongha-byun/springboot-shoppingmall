package springboot.shoppingmall.dto.category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.domain.product.Category;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryResponse {

    private Long id;
    private String name;
    private List<CategoryResponse> subCategories = new ArrayList<>();

    public CategoryResponse(Long id, String name, List<CategoryResponse> subCategories) {
        this.id = id;
        this.name = name;
        this.subCategories = subCategories;
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName(),
                category.getSubCategories().stream()
                        .map(CategoryResponse::of)
                        .collect(Collectors.toList()));
    }

    public List<CategoryResponse> getSubCategories() {
        return subCategories;
    }
}
